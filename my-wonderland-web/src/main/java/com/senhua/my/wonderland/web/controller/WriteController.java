package com.senhua.my.wonderland.web.controller;

import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.entity.UserContent;
import com.senhua.my.wonderland.web.service.SolrService;
import com.senhua.my.wonderland.web.service.UserContentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by Administrator on 2019/9/4.
 */
@Controller
public class WriteController extends BaseController {
    private final static Logger log = Logger.getLogger(WriteController.class);

    @Autowired
    private UserContentService userContentService;
    @Autowired
    private SolrService solrService;

    @RequestMapping("/writedream")
    public String writedream(Model model,@RequestParam(value = "cid",required = false) Long cid){
        User user = getCurrentUser();
        if(cid != null){
            UserContent userContent = userContentService.findById(cid);
            model.addAttribute("cont",userContent);
        }
        model.addAttribute("user",user);
        return "write/writedream";
    }

    @RequestMapping("/doWritedream")
    public String doWritedream(Model model, @RequestParam(value = "id",required = false)String id,
                               @RequestParam(value = "cid",required = false) Long cid,
                               @RequestParam(value = "category",required = false)String category,
                               @RequestParam(value = "txt_Title",required = false)String txt_Title,
                               @RequestParam(value = "content",required = false)String content,
                               @RequestParam(value = "private_dream",required = false)String private_dream){

        log.info("进入写梦Controller");
        User user = getCurrentUser();
        if(user == null){
            //未登录
            model.addAttribute("error","请先登录！");
            return "../login";
        }
        UserContent userContent = new UserContent();
        if(cid!=null){
            userContent = userContentService.findById(cid);
        }
        userContent.setCategory(category);
        userContent.setContent(content);
        userContent.setTitle(txt_Title);
        userContent.setRptTime(new Date());
        String imgUrl = user.getImgUrl();
        if(StringUtils.isBlank(imgUrl)){
            userContent.setImgUrl("/image/icon_m.jpg");
        }else{
            userContent.setImgUrl(imgUrl);
        }

        if("on".equals(private_dream)){
            userContent.setPersonal("1");
        }else{
            userContent.setPersonal("0");
        }
        userContent.setTitle(txt_Title);
        userContent.setuId(user.getId());
        userContent.setNickName(user.getNickName());
        if(cid == null){
            userContent.setUpvote(0);
            userContent.setDownvote(0);
            userContent.setCommentNum(0);
            userContentService.addContent(userContent);
            solrService.addUserContent(userContent);

        }else{
            userContentService.updateById(userContent);
            solrService.updateUserContent(userContent);
        }

        model.addAttribute("content",userContent);
        return "write/writesuccess";
    }


}
