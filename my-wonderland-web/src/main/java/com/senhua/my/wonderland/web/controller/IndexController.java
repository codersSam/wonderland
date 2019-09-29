package com.senhua.my.wonderland.web.controller;

import com.senhua.my.wonderland.web.common.DateUtils;
import com.senhua.my.wonderland.web.common.PageHelper.Page;
import com.senhua.my.wonderland.web.common.StringUtil;
import com.senhua.my.wonderland.web.entity.Comment;
import com.senhua.my.wonderland.web.entity.Upvote;
import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.entity.UserContent;
import com.senhua.my.wonderland.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by Administrator on 2019/7/26.
 */
@Controller
public class IndexController extends BaseController {

    private final static Logger log = Logger.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserContentService userContentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UpvoteService upvoteService;

    @Autowired
    private SolrService solrService;

    @RequestMapping("/index_list")
    public String findAllList(Model model, @RequestParam(value = "keyword",required = false)String keyword,
                              @RequestParam(value = "pageNum",required = false)Integer pageNum,
                              @RequestParam(value = "pageSize",required = false)Integer pageSize){
        log.info("==============进入index_list==============");
        User user = getCurrentUser();
        if(user != null){
            model.addAttribute("user",user);
        }
        if(StringUtils.isNotBlank(keyword)){
            Page<UserContent> page = solrService.findByKeyWords(keyword, pageNum, pageSize);
            model.addAttribute("keyword",keyword);
            model.addAttribute("page",page);
        }else{
            Page<UserContent> page = findAll(pageNum,pageSize);
            model.addAttribute("page",page);
        }
        return "../index";
    }

    /**
     * 点赞或踩功能
     * @param model
     * @param id
     * @param uid
     * @param upvote
     * @return
     */
    @RequestMapping("/upvote")
    @ResponseBody
    public Map<String,Object> upvote(Model model,@RequestParam(value = "id",required = false)Long id,
                                     @RequestParam(value = "uid",required = false)Long uid,
                                     @RequestParam(value = "upvote",required = false)int upvote){
        log.info("id=" + id + ",udi=" + uid + "upvote" +upvote);
        Map map = new HashMap<String,Object>();
        User user = getCurrentUser();
        if(user == null){
            map.put("data","fail");
            return map;
        }

        Upvote up = new Upvote();
        up.setContentId(id);
        up.setuId(user.getId());
        //当前用户是否已经存在点赞或踩操作，存在则使用，不存在则使用up对象封装创建
        Upvote upv = upvoteService.findByUidAndConId(up);
        if(upv != null){
            log.info(upv.toString() + "=======================");
        }
        UserContent userContent = userContentService.findById(id);
        if(upvote == -1){
            if(upv != null){
                if("1".equals(upv.getDownvote())){
                    map.put("data","down");
                    return map;
                }else{
                    upv.setDownvote("1");
                    upv.setUpvoteTime(new Date());
                    upv.setIp(getClientIpAddress());
                    upvoteService.update(upv);
                }
            }else{
                up.setDownvote("1");
                up.setUpvoteTime(new Date());
                up.setIp(getClientIpAddress());
                upvoteService.add(up);
            }

            userContent.setDownvote(userContent.getDownvote() + upvote);
        }else{
            if(upv != null){
                if("1".equals(upv.getUpvote())){
                    map.put("data","done");
                    return map;
                }else{
                    upv.setUpvote("1");
                    upv.setUpvoteTime(new Date());
                    upv.setIp(getClientIpAddress());
                    upvoteService.update(upv);
                }
            }else{
                up.setUpvote("1");
                up.setUpvoteTime(new Date());
                up.setIp(getClientIpAddress());
                upvoteService.add(up);
            }
            userContent.setUpvote(userContent.getUpvote() + upvote);
        }
        userContentService.updateById(userContent);
        map.put("data","success");
        return map;
    }

    /**
     * （1）先根据文章 id 查找出所有的一级评论列表。
     （2）遍历一级评论列表，根据文章 id 和一级评论的子评论 id（多个id用 , 号隔开）字符串查询子评论列表。
     （3）遍历子评论列表，如果评论者 id 不为 null，则根据该 id 查询 User，注入子评论的 byUser 属性中。
     （4）将子评论列表注入到一级评论的 comList 属性中。
     （5）将 list 放入 map 中，返回 map。
     * @param model
     * @param content_id
     * @return
     */
    @RequestMapping("/reply")
    @ResponseBody
    public Map<String,Object> reply(Model model,@RequestParam(value = "content_id",required = false)Long content_id){
        Map map = new HashMap<String,Object>();
        List<Comment> list = commentService.findAllFirstComment(content_id);
        if(list != null && list.size()>0){
            for(Comment c: list){
                List<Comment> comments = commentService.findAllChildrenComment(c.getConId(),c.getChildren());
                if(comments != null && comments.size()>0){
                    for(Comment com:comments){
                        if(com.getById()!= null){
                            User byUser = userService.findById(com.getById());
                            com.setByUser(byUser);
                        }
                    }
                }
                c.setComList(comments);
            }
        }
        map.put("list" , list);

        return map;
    }

    @RequestMapping("/comment")
    @ResponseBody
    public Map<String,Object> comment(Model model,@RequestParam(value = "id",required = false)Long id,
                                      @RequestParam(value = "content_id",required = false)Long content_id,
                                      @RequestParam(value = "uid",required = false)Long uid,
                                      @RequestParam(value = "bid",required = false)Long bid,
                                      @RequestParam(value = "oSize",required = false)String oSize,
                                      @RequestParam(value = "comment_time",required = false)String comment_time,
                                      @RequestParam(value = "upvote",required = false)Integer upvote){
        Map map = new HashMap<String,Object>();
        User user = getCurrentUser();
        if(user == null){
            map.put("data","fail");
            return map;
        }
        if(id == null){
            Date date = DateUtils.StringToDate(comment_time,"yyyy-MM-dd HH:mm:ss");
            Comment comment = new Comment();
            comment.setComContent(oSize);
            comment.setCommTime(date);
            comment.setConId(content_id);
            comment.setComId(uid);
            if(upvote == null){
                upvote = 0;
            }
            comment.setById(bid);
            comment.setUpvote(upvote);
            User u = userService.findById(uid);
            comment.setUser(u);
            commentService.add(comment);
            map.put("data",comment);

            UserContent userContent = userContentService.findById(content_id);
            Integer num = userContent.getCommentNum();

            userContent.setCommentNum(num + 1);
            userContentService.updateById(userContent);

        }
        else{
            //点赞
            Comment c = commentService.findById(id);
            c.setUpvote(upvote);
            commentService.update(c);
        }

        return map;

    }

    /**
     *
     * （1）从 Session 中取出用户信息，判断用户是否登录，未登录，则返回 fail。
     * （2）将日期字符串转成自定义格式的日期 Date，如果 upvote 为 null，则赋初始值0，将 comment 信息设置完毕之后插入数据库，返回主键 id，此时该 comment 中 id 是有值的。
     * （3）根据评论 id 查询出评论对象 com，判断该评论对象 com 是否有子评论，如果没有，将刚才添加的子评论 id 添加到 Comment 的 children 字段中。
     * （4）如果该评论对象 com 已有子评论了，则将刚才添加的子评论的 id 以逗号形式拼接在子评论 id 后面。
     * （5）更新该评论对象 com。
     * （6）将评论对象 comment 放入 map。
     * （7）根据文章 id 查询出文章对象 userContent，获取评论数，将其值加1，更新 userContent。
     * （8）最后将 map 返回给页面。
     * @param model
     * @param id
     * @param content_id
     * @param uid
     * @param bid
     * @param oSize
     * @param comment_time
     * @param upvote
     * @return
     */
    @RequestMapping("/comment_child")
    @ResponseBody
    public Map<String,Object> addCommentChild(Model model,@RequestParam(value = "id",required = false)Long id,
                                              @RequestParam(value = "content_id",required = false)Long content_id,
                                              @RequestParam(value = "uid",required = false)Long uid,
                                              @RequestParam(value = "by_id",required = false)Long bid,
                                              @RequestParam(value = "oSize",required = false)String oSize,
                                              @RequestParam(value = "comment_time",required = false)String comment_time,
                                              @RequestParam(value ="upvote",required = false)Integer upvote){
        Map map = new HashMap<String,Object>();
        User user = getCurrentUser();
        if(user == null){
            map.put("data","fail");
            return map;
        }

        Date date = DateUtils.StringToDate(comment_time,"yyyy-MM-dd HH:mm:ss");

        Comment comment = new Comment();
        comment.setComContent(oSize);
        comment.setCommTime(date);
        comment.setConId(content_id);
        comment.setComId(uid);

        if(upvote == null){
            upvote = 0;
        }

        comment.setById(bid);
        comment.setUpvote(upvote);
        User u = userService.findById(uid);
        comment.setUser(u);
        commentService.add(comment);

        Comment com = commentService.findById(id);
        if(StringUtils.isBlank((com.getChildren()))){
            com.setChildren(comment.getId().toString());
        }else{
            com.setChildren(com.getChildren() + "," + comment.getId());
        }
        commentService.update(com);
        map.put("data",comment);

        UserContent userContent = userContentService.findById(content_id);
        Integer num = userContent.getCommentNum();
        userContent.setCommentNum(num+1);

        userContentService.updateById(userContent);
        return map;
    }

    /**
     * 删除评论
     * @param model
     * @param id
     * @param uid
     * @param con_id
     * @param fid
     * @return
     */
    @RequestMapping("/deleteComment")
    @ResponseBody
    public Map<String,Object> deleteComment(Model model,@RequestParam(value = "id",required = false)Long id,
                                            @RequestParam(value = "uid",required = false) Long uid,
                                            @RequestParam(value = "con_id",required = false) Long con_id,
                                            @RequestParam(value = "fid",required = false) Long fid){
        int num = 0;
        Map map = new HashMap<String,Object>();
        User user = getCurrentUser();
        if(user == null){
            map.put("data","fail");
        }else{
            if(user.getId().equals(uid)){
                Comment comment = commentService.findById(id);
                if(StringUtils.isBlank(comment.getChildren())){
                    if(fid != null){
                        Comment fcomm = commentService.findById(id);
                        String child = StringUtil.getString(fcomm.getChildren(),id);
                        fcomm.setChildren(child);
                        commentService.update(fcomm);
                    }
                    commentService.deleteById(id);
                    num = num + 1;
                }else{
                    String children = comment.getChildren();
                    commentService.deleteChildrenComment(children);
                    String[] arr = children.split(",");

                    commentService.deleteById(id);

                    num = num + arr.length + 1;
                }

                UserContent content = userContentService.findById(con_id);
                if(content != null){
                    if(content.getCommentNum() - num >= 0){
                        content.setCommentNum(content.getCommentNum() - num);
                    }else{
                        content.setCommentNum(0);
                    }

                    userContentService.updateById(content);
                }
                map.put("data",content.getCommentNum());
            }else{
                map.put("data","no-access");
            }

        }
        return map;
    }

}
