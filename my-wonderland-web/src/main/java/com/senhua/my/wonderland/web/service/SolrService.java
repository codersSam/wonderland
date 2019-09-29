package com.senhua.my.wonderland.web.service;

import com.senhua.my.wonderland.web.common.PageHelper;
import com.senhua.my.wonderland.web.entity.UserContent;

/**
 * Created by Administrator on 2019/9/16.
 */
public interface SolrService {
    /**
     * 根据关键字搜索文章并分页
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageHelper.Page<UserContent> findByKeyWords(String keyword,Integer pageNum,Integer pageSize);

    /**
     * 根据solr索引库
     * @param userContent
     */
    void addUserContent(UserContent userContent);

    /**
     * 根据文章id删除索引库
     * @param userContent
     */
    void updateUserContent(UserContent userContent);

    /**
     * 根据文章id索引库
     * @param id
     */
    void deleteById(Long id);
}
