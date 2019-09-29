package com.senhua.my.wonderland.web.dao;

import com.senhua.my.wonderland.web.entity.UserContent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * Created by Administrator on 2018/1/9.
 */
@Repository
public interface UserContentMapper extends Mapper<UserContent> {

    /**
     * 根据用户id查询出梦分类
     * @param uid
     * @return
     */
    List<UserContent> findCategoryByUid(@Param("uid") long uid);

    /**
     * 插入文章并返回主键id返回类型只是影响行数 id在UserContent对象中
     * @param userContent
     * @return
     */
    int insertContent(UserContent userContent);

    /**
     * user_content与user连接查询
     * @param userContent
     * @return
     */
    List<UserContent> findByJoin(UserContent userContent);

}
