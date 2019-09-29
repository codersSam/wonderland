package com.senhua.my.wonderland.web.dao;

import com.senhua.my.wonderland.web.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */
@Repository
public interface CommentMapper extends Mapper<Comment> {
    //根据文章id查询所有评论
    List<Comment> selectAll(@Param("cid") long cid);
    //根据文章id查询所有一级评论
    List<Comment> findAllFirstComment(@Param("cid") long cid);
    //根据文章id和二级评论ids查询出所有二级评论
    List<Comment> findAllChildrenComment(@Param("cid") long cid, @Param("children") String children);
    //插入评论并返回主键id 返回类型只是影响行数  id在Comment对象中
    int insertComment(Comment comment);

}
