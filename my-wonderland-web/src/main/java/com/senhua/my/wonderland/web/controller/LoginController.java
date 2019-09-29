package com.senhua.my.wonderland.web.controller;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.senhua.my.wonderland.web.common.Constants;
import com.senhua.my.wonderland.web.common.MD5Util;
import com.senhua.my.wonderland.web.common.RandStringUtils;
import com.senhua.my.wonderland.web.entity.OpenUser;
import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.entity.UserInfo;
import com.senhua.my.wonderland.web.service.OpenUserService;
import com.senhua.my.wonderland.web.service.UserInfoService;
import com.senhua.my.wonderland.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2019/7/24.
 */
@Controller
public class LoginController extends BaseController {

    private final static Logger log = Logger.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private OpenUserService openUserService;
    @Autowired
    private UserInfoService userInfoService;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    /**
     * 登录跳转
     *
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String login(Model model) {
        User user = (User) getSession().getAttribute("user");
        if (user != null) {
            return "/personal/personal";
        }
        return "../login";
    }

    /**
     * 发送短信验证码
     * @param model
     * @param telephone
     * @return
     */
    @RequestMapping("/sendSms")
    @ResponseBody
    public Map<String, Object> index(Model model, @RequestParam(value = "telephone", required = false) final String telephone) {
        Map map = new HashMap<String, Object>();
        try {
            final String code = RandStringUtils.getCode();
            redisTemplate.opsForValue().set(telephone, code, 360, TimeUnit.SECONDS);
            //调用ActiveMQ jmsTemplate,发送一条消息给MQ
            jmsTemplate.send("login_msg", new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("telephone", telephone);
                    mapMessage.setString("code", code);
                    return mapMessage;
                }
            });
        } catch (Exception e) {
            map.put("msg", false);
        }
        map.put("msg", true);
        return map;
    }

    /**
     * 登陆
     * @param model
     * @param email
     * @param password
     * @param code
     * @param telephone
     * @param phoneCode
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
     @RequestMapping("/doLogin")
    public String doLogin(Model model, @RequestParam(value = "username", required = false) String email,
                          @RequestParam(value = "password", required = false) String password,
                          @RequestParam(value = "code", required = false) String code,
                          @RequestParam(value = "telephone", required = false) String telephone,
                          @RequestParam(value = "phone_code", required = false) String phoneCode,
                          @RequestParam(value = "state", required = false) String state,
                          @RequestParam(value = "pageNum", required = false) Integer pageNum,
                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (StringUtils.isNotBlank(telephone)) {
            //手机登陆
            String yzm = redisTemplate.opsForValue().get(telephone);
            if (phoneCode.equals(yzm)) {
                //验证码正确
                User user = userService.findByPhone(telephone);
                getSession().setAttribute("user", user);
                model.addAttribute("user", user);
                log.info("手机快捷登陆成功");
                return "redirect:/personal";
            } else {
                //验证码错误或过期
                model.addAttribute("error", "phone_fail");
                return "../login";
            }
        } else {
            if (StringUtils.isBlank(code)) {
                model.addAttribute("error", "fail");
                return "../login";
            }
            int b = checkValidateCode(code);
            if (b == -1) {
                model.addAttribute("error", "fail");
                return "../login";
            } else if (b == 0) {
                model.addAttribute("error", "fail");
                return "../login";
            }

            password = MD5Util.encodeToHex(Constants.SALT + password);
            User user = userService.login(email, password);
            if (user != null) {
                if ("0".equals(user.getState())) {
                    //未激活
                    model.addAttribute("email", email);
                    model.addAttribute("error", "active");
                    return "../login";
                }
                log.info("用户登录成功");
                getSession().setAttribute("user", user);
                model.addAttribute("user", user);
                return "redirect:/list";
            } else {
                log.info("用户登录失败");
                model.addAttribute("email", email);
                model.addAttribute("error", "fail");
                return "../login";
            }
        }

    }


    @RequestMapping("to_login")
    public String toLogin(Model model){
        HttpServletRequest request =getRequest();
        String url = "";
        try {
            url = new Oauth().getAuthorizeURL(request);
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
        return "redirect:" + url;
    }

    @RequestMapping("qq_login")
    public String qqLogin(Model model){
        User user = (User) getSession().getAttribute("user");
        boolean flag = false;
        try {
            AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(getRequest());
            String accessToken = null, openID = null;
            long tokenExpireIn = 0L;
            if(accessTokenObj.getAccessToken().equals("")){
                System.out.print("没有获取到响应参数");
            }else{
                //授权令牌token
                accessToken = accessTokenObj.getAccessToken();
                //过期时间
                tokenExpireIn = accessTokenObj.getExpireIn();
                //利用获取到的accessToken去获取当前的openid---------start
                OpenID openIDObj = new OpenID(accessToken);
                //用户唯一的标识
                openID = openIDObj.getUserOpenID();
                //利用获取到的accessToken去获取当前的openid---------end
                com.qq.connect.api.qzone.UserInfo qzoneUserInfo = new com.qq.connect.api.qzone.UserInfo(accessToken,openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                if(userInfoBean.getRet() == 0){
                    OpenUser openUser =  openUserService.findByOpenId(openID);
                    if(userInfoBean == null){
                        redisTemplate.opsForValue().set(openID,accessToken,90, TimeUnit.DAYS);
                        openUser = new OpenUser();
                        if(user == null){
                            flag = true;
                            user.setEmail(openID);
                            user.setPassword(MD5Util.encodeToHex(Constants.SALT + accessToken));
                            user.setEnable("1");
                            user.setState("0");
                            //获取qq昵称
                            user.setNickName(userInfoBean.getNickname());
                            //获取qq头像
                            user.setImgUrl(userInfoBean.getAvatar().getAvatarURL50());
                            userService.regist(user);
                        }
                        openUser.setOpenId(openID);
                        openUser.setAccessToken(accessToken);
                        openUser.setAvatar(userInfoBean.getAvatar().getAvatarURL50());
                        openUser.setExpiredTime(tokenExpireIn);
                        openUser.setNickName(userInfoBean.getNickname());
                        openUser.setOpenType(Constants.OPEN_TYPE_QQ);
                        openUser.setuId(user.getId());
                        openUserService.add(openUser);
                    }else{
                        //从redis获取accessToken
                        String token = redisTemplate.opsForValue().get(openID);
                        if(token == null){
                            openUser.setAccessToken(accessToken);
                            openUser.setAvatar(userInfoBean.getAvatar().getAvatarURL50());
                            openUser.setExpiredTime(tokenExpireIn);
                            openUserService.update(openUser);
                        }
                        user = userService.findById(openUser.getuId());
                    }
                }else{
                    log.info("很抱歉，我们没能正确获取到您的信息，原因是：" + userInfoBean.getMsg());
                }
            }
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
        getSession().setAttribute("user",user);
        if(flag){
            return "rediect:/list";
        }else{
            model.addAttribute("qq",Constants.OPEN_TYPE_QQ);
            UserInfo userInfo = userInfoService.findByUid(user.getId());
            model.addAttribute("user",user);
            model.addAttribute("userInfo",userInfo);
            return "personal/profile";
        }
    }

    @RequestMapping("/remove_qq")
    public String removeQQ(Model model){
        User user = (User) getSession().getAttribute("user");
        if(user == null){
            return "../login";
        }
        openUserService.deleteByUidAndType(user.getId(),Constants.OPEN_TYPE_QQ);
        List<OpenUser> openUsers = openUserService.findByUid(user.getId());
        setAttribute(openUsers,model);
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        model.addAttribute("user",user);
        model.addAttribute("userInfo",userInfo);
        return "personal/profile";
    }

    public void setAttribute(List<OpenUser> openUsers, Model model) {
        if(openUsers != null && openUsers.size() > 0){
            for(OpenUser openUser:openUsers){
                if(Constants.OPEN_TYPE_QQ.equals(openUser.getOpenType())){
                    model.addAttribute("qq",openUser.getOpenType());
                }else if(Constants.OPEN_TYPE_WEIBO.equals(openUser.getOpenType())){
                    model.addAttribute("weibo",openUser.getOpenType());
                }else if(Constants.OPEN_TYPE_WEIXIN.equals(openUser.getOpenType())){
                    model.addAttribute("weixin",openUser.getOpenType());
                }
            }
        }
    }

    /**
     * 判断当前session是否还有code
     * @param code
     * @return  1      session的code过期
     *         - 1      输入的code与session的code匹配错误
     */
    private int checkValidateCode(String code) {
        Object vercode = getRequest().getSession().getAttribute(Constants.VERCODE_KEY);
        if (null == vercode) {
            return -1;
        }
        if (!code.equalsIgnoreCase(vercode.toString())) {
            return 0;
        }
        return 1;
    }


    /**
     * 退出登录
     * @param model
     * @return
     */
    @RequestMapping("/loginout")
    public String exit(Model model){
        log.info("退出登录");
        getSession().removeAttribute("user");
        getSession().invalidate();
        return "../login";
    }


}
