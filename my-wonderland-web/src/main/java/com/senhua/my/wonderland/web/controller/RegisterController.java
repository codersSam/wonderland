package com.senhua.my.wonderland.web.controller;

import com.senhua.my.wonderland.web.common.CodeCaptchaServlet;
import com.senhua.my.wonderland.web.common.MD5Util;
import com.senhua.my.wonderland.web.entity.Role;
import com.senhua.my.wonderland.web.entity.RoleUser;
import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.mail.SendEmail;
import com.senhua.my.wonderland.web.service.RoleUserService;
import com.senhua.my.wonderland.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019/7/21.
 */
@Controller
public class RegisterController {
    private final static Logger log = Logger.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    //redis数据库操作模板
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private RoleUserService roleUserService;

    @RequestMapping("register")
    public String toRegister(){
        return "../register";
    }

    /**
     * ajax校验手机是否注册
     * @param model
     * @param phone
     * @return
     */
    @RequestMapping("/checkPhone")
    @ResponseBody
    public Map<String ,Object> checkPhone(Model model , @RequestParam(value = "phone" ,required = false) String phone){
        log.debug("注册-判断手机号" + phone + "是否可用");
        Map map = new HashMap<String,Object>();
        User user = userService.findByPhone(phone);
        if(user == null){
            //未注册
            map.put("message","success");
        }else{
            //已注册
            map.put("message","fail");
        }
        return map;
    }

    /**
     * 判断邮箱是否注册
     * @param model
     * @param email
     * @return
     */
    @RequestMapping("/checkEmail")
    @ResponseBody
    public Map<String,Object> checkEmail(Model model ,@RequestParam(value = "email",required = false)String email){
        log.debug("注册-判断邮箱" + email + "对否可用");
        Map map =new HashMap<String,Object>();
        User user = userService.findByEmail(email);
        if(user == null){
            //未注册
            map.put("message","success");
        }else{
            //已注册
            map.put("message","fail");
        }
        return map;
    }

    /**
     * 验证码校验
     * @param model
     * @param code
     * @return
     */
    @RequestMapping("/checkCode")
    @ResponseBody
    public Map<String,Object> checkCode(Model model,@RequestParam(value = "code",required = false) String code){
        log.debug("注册-判断验证码" + code + "是否可用");
        Map map = new HashMap<String,Object>();
        ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        String vcode = (String) attrs.getRequest().getSession().getAttribute(CodeCaptchaServlet.VERCODE_KEY);
        if(code.equals(vcode)){
            //验证码正确
            map.put("message","success");
        }else{
            //验证码错误
            map.put("message","fail");
        }
        return map;
    }


    /**
     * 注册提交处理
     * @param model
     * @param email
     * @param password
     * @param phone
     * @param nickName
     * @param code
     * @return
     */
    @RequestMapping(value = "/doRegister",method = RequestMethod.POST)
    public String doRegister(Model model,@RequestParam(value = "email",required = false) String email,
                             @RequestParam(value = "password",required = false) String password,
                             @RequestParam(value = "phone",required = false) String phone,
                             @RequestParam(value = "nickName",required = false) String nickName,
                             @RequestParam(value = "code",required = false) String code){
        log.debug("注册...");
        if(StringUtils.isBlank(code)){
            model.addAttribute("error","非法注册，请重新注册！");
            return "../register";
        }
        int b = checkValidateCode(code);
        if(b == -1){
            model.addAttribute("error","验证码超时，请重新登录！");
            return "../register";
        }else if(b == 0 ){
            model.addAttribute("error","验证码不正确，请重新输入！");
            return "../register";
        }

        User user = userService.findByEmail(email);
        if(user != null){
            model.addAttribute("error","该用户已被注册！");
            return "../register";
        }else{
            user = new User();
            user.setNickName(nickName);
            user.setPassword(new Md5PasswordEncoder().encodePassword(password,email));
            user.setPhone(phone);
            user.setEmail(email);
            user.setState("0");
            user.setEnable("0");
            user.setImgUrl("/images/icon_m.jpg");

            //邮箱激活码
            String validateCode = MD5Util.encodeToHex("salt" + email + password);
            boolean isSend = SendEmail.sendEmailMessage(email, validateCode);
            if(isSend){
                redisTemplate.opsForValue().set(email,validateCode,1, TimeUnit.HOURS);
                userService.regist(user);

                log.info("注册成功");

                String message = email + "," + validateCode;
                model.addAttribute("message",message);

                return "/regist/registerSuccess";
            }else{
                model.addAttribute("EmailFail","你的邮箱地址不正确，请重新输入");
                return "/regist";
            }

        }

    }
    /**
     * 匹配验证码的正确性
     * @param code
     * @return
     */
    private int checkValidateCode(String code) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object vercode = attrs.getRequest().getSession().getAttribute(CodeCaptchaServlet.VERCODE_KEY);
        if(vercode == null){
            return -1;
        }
        if(!code.equalsIgnoreCase(vercode.toString())){
            return 0;
        }
        return 1;
    }

    /**
     * 重新发送激活邮件
     * @param model
     * @return
     */
    @RequestMapping("/sendEmail")
    @ResponseBody
    public Map<String ,Object> sendEmail(Model model){
        Map map = new HashMap<String ,Object>();
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String validateCode = attrs.getRequest().getParameter("validateCode");
        String email = attrs.getRequest().getParameter("email");
        boolean isSend = SendEmail.sendEmailMessage(email, validateCode);
        if(isSend){
            map.put("message","success");
        }
        else{
            map.put("message","fail");
        }
        return map;
    }

    @RequestMapping("/activecode")
    public String active(Model model){
        log.info("================激活验证=================");
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String validateCode = attrs.getRequest().getParameter("validateCode");
        String email = attrs.getRequest().getParameter("email");
        String code = redisTemplate.opsForValue().get(email);
        log.info("验证邮箱为：" + email + "，邮箱激活码为：" + code + ",用户链接的激活码为：" + validateCode);

        //判断是否已激活
        User userTrue = userService.findByEmail(email);
        if(userTrue != null && "1".equals(userTrue.getState())){
            model.addAttribute("success","您已激活，请直接登录！");
            return "../login";
        }
        if(code == null){
            //激活码过期
            model.addAttribute("fail","您的激活码一过期，请重新注册");
            userService.deleteByEmail(email);
            return "/regist/activeFail";
        }
        if(StringUtils.isNotBlank(validateCode) && validateCode.equals(code)){
            //激活码正确
            userTrue.setState("1");
            userTrue.setEnable("1");
            userService.update(userTrue);
            model.addAttribute("email",email);
            RoleUser roleUser = new RoleUser();
            roleUser.setuId(userTrue.getId());
            roleUser.setrId(1L);
            roleUserService.add(roleUser);
            return "/regist/activeSuccess";
        }else{
            //激活码错误
            model.addAttribute("fail","您的激活码错误，请重新激活！");
            return "/regist/activeFail";
        }
    }



}
