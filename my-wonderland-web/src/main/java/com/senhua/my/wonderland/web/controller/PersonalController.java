package com.senhua.my.wonderland.web.controller;

import com.senhua.my.wonderland.web.common.Constants;
import com.senhua.my.wonderland.web.common.DateUtils;
import com.senhua.my.wonderland.web.common.MD5Util;
import com.senhua.my.wonderland.web.common.PageHelper;
import com.senhua.my.wonderland.web.entity.OpenUser;
import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.entity.UserContent;
import com.senhua.my.wonderland.web.entity.UserInfo;
import com.senhua.my.wonderland.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/9/3.
 */
@Controller
public class PersonalController extends BaseController {


    private final static Logger log = Logger.getLogger(PersonalController.class);

    @Autowired
    private UserContentService userContentService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UpvoteService upvoteService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private OpenUserService openUserService;
    @Autowired
    private SolrService solrService;

    /**
     * 点击获取user详细信息
     * @param model
     * @return
     */
    @RequestMapping("/profile")
    public String profile(Model model,@RequestParam(value = "email",required = false)String email,
                          @RequestParam(value = "password",required = false)String password,
                          @RequestParam(value = "phone",required = false)String phone){
        User user = getCurrentUser();
        if(user == null) {
            return "../login";
        }
        if(StringUtils.isNotBlank(email)){
            user.setEmail(email);
            user.setPassword(MD5Util.encodeToHex(Constants.SALT + password));
            user.setPhone(phone);
            userService.update(user);
        }
        List<OpenUser> openUsers = openUserService.findByUid(user.getId());
        if(openUsers != null && openUsers.size()>0){
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
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        model.addAttribute("user",user);
        model.addAttribute("userInfo",userInfo);

        return "personal/profile";
    }

    @RequestMapping("saveImage")
    @ResponseBody
    public Map<String,Object> saveImage(Model model,
                                        @RequestParam(value ="url",required = false)String url){
        Map map = new HashMap<String,Object>();
        User user = getCurrentUser();
        user.setImgUrl(url);
        userService.update(user);
        map.put("msg","success");
        return map;
    }

    @RequestMapping("/saveUserInfo")
    public String saveUserInfo(Model model,@RequestParam(value = "name",required = false)String name,
                               @RequestParam(value = "nick_name",required = false)String nickName,
                               @RequestParam(value = "sex",required = false)String sex,
                               @RequestParam(value = "address",required = false)String address,
                               @RequestParam(value = "birthday",required = false)String birthday){
        User user = getCurrentUser();
        if(user == null){
            return "../login";
        }
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        boolean flag = false;
        if(userInfo == null){
            userInfo = new UserInfo();
        }else{
            flag = true;
        }
        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setSex(sex);
        Date bir = DateUtils.StringToDate(birthday,"yyyy-MM-dd");
        userInfo.setBirthday(bir);
        userInfo.setuId(user.getId());
        if(!flag){
            userInfoService.add(userInfo);
        }else{
            userInfoService.update(userInfo);
        }
        user.setNickName(nickName);
        userService.update(user);
        userInfo = userInfoService.findByUid(user.getId());
        model.addAttribute("user",user);
        model.addAttribute("userInfo",userInfo);
        return "personal/profile";
    }

    @RequestMapping("/repassword")
    public String repassword(Model model){
        User user = getCurrentUser();
        if(user != null){
            model.addAttribute("user",user);
            return "personal/repassword";
        }
        return "../login";
    }

    @RequestMapping("/updatePassword")
    public String updatePassword(Model model,@RequestParam(value = "old_password",required = false)String oldPassword,
                                 @RequestParam(value = "password",required = false)String password){
        User user = getCurrentUser();
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        if(user != null){
            oldPassword = md5PasswordEncoder.encodePassword(oldPassword, user.getEmail());
            if(user.getPassword().equals(oldPassword)){
                password = md5PasswordEncoder.encodePassword(password, user.getEmail());
                user.setPassword(password);
                userService.update(user);
                model.addAttribute("message","success");
            }else{
                model.addAttribute("message","fail");
            }
        }
        model.addAttribute("user",user);
        return "personal/passwordSuccess";
    }

    /**
     * 初始化个人主页数据
     * @param model
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public String findList(Model model ,@RequestParam(value ="id",required = false)String id,
                           @RequestParam(value = "pageNum",required = false)Integer pageNum,
                           @RequestParam(value = "pageSize",required = false)Integer pageSize,
                           @RequestParam(value = "manage",required = false) String manage ){
        User user = getCurrentUser();
        UserContent userContent = new UserContent();
        UserContent uc = new UserContent();

        if(user != null){
            model.addAttribute("user",user);
            userContent.setuId(user.getId());
            uc.setuId(user.getId());
        }else{
            return "../login";
        }
        if(StringUtils.isNotBlank(manage)){
            model.addAttribute("manage",manage);
        }
        log.info("初始化个人主页信息");
        //查询分类
        List<UserContent> categorys = userContentService.findCategoryByUid(user.getId());
        model.addAttribute("categorys",categorys);
        //发布的梦，不含私密梦
        userContent.setPersonal("0");
        //默认每页显示4条信息
        pageSize = 4;
        PageHelper.Page<UserContent> page = findAll(userContent,pageNum,pageSize);

        model.addAttribute("page",page);



        //查询私密梦
        uc.setPersonal("1");;
        PageHelper.Page<UserContent> page2 = findAll(uc,pageNum,pageSize);
        model.addAttribute( "page2",page2 );

        //查询热梦
        UserContent uct = new UserContent();
        uct.setPersonal("0");
        PageHelper.Page<UserContent> hotPage = findAllByUpvote(uct,pageNum,pageSize);
        model.addAttribute("hotPage",hotPage);


        return  "personal/personal";

    }

    @RequestMapping("/findByCategory")
    @ResponseBody
    public Map<String,Object> findByCategory(Model model,
                                             @RequestParam(value = "category",required = false)String category,
                                             @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                             @RequestParam(value = "pageSize", required = false)Integer pageSize){

        Map map = new HashMap<String,Object>();
        User user = getCurrentUser();

        if(user == null){
            map.put("pageCate","fail");
            return map;
        }
        pageSize = 4;
        PageHelper.Page<UserContent> pageCate = userContentService.findByCategory(category,user.getId(),pageNum,pageSize);

        map.put("pageCate",pageCate);

        return map;
    }

    /**
     * 根据用户id查询私密梦
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPersonal")
    @ResponseBody
    public Map<String,Object> findPersonal(Model model,
                                           @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                           @RequestParam(value = "pageSize",required = false)Integer pageSize){
        Map map = new HashMap<String,Object>();
        User user = getCurrentUser();
        if(user == null){
            map.put("page2","fail");
            return map;
        }
        pageSize = 4;
        PageHelper.Page<UserContent> page = userContentService.findPersonal(user.getId(),pageNum,pageSize);
        map.put("page2",page);

        return map;
    }

    /**
     * 查询出所有文章并分页 根据点赞数倒叙排列
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findAllHotContents")
    @ResponseBody
    public Map<String,Object> findAllHotContents(Model model,
                                                 @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                 @RequestParam(value = "pageSize",required = false)Integer pageSize){
        Map map = new HashMap<String,Object>();
        User user = getCurrentUser();
        if(user == null){
            map.put("hotPage","fail");
            return map;
        }
        pageSize = 4;
        UserContent uct = new UserContent();
        uct.setPersonal("0");
        PageHelper.Page<UserContent> hotPage = findAllByUpvote(uct,pageNum,pageSize);
        map.put("hotPage",hotPage);
        return map;
    }

    /**
     * 根据文章id查找文章
     * @param model
     * @param cid
     * @return
     */
    @RequestMapping("/watch")
    public String watchContent(Model model,@RequestParam(value = "cid",required = false)Long cid){
        User user = getCurrentUser();
        if(user == null){
            //未登录
            model.addAttribute("error","请先登录！");
            return "../login";
        }
        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("cont",userContent);
        model.addAttribute("user",user);
        return "personal/watch";
    }

    @RequestMapping("/deleteContent")
    public String deleteContent(Model model,
                                @RequestParam(value = "cid",required = false) Long cid){
        User user = getCurrentUser();
        if(user == null){
            return "../login";
        }
        commentService.deleteByContentId(cid);
        upvoteService.deleteByContentId(cid);
        userContentService.deleteById(cid);
        solrService.deleteById(cid);
        return "redirect:/list?manage=manage";
    }


}
