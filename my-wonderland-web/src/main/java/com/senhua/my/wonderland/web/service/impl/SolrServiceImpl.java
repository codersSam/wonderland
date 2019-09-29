package com.senhua.my.wonderland.web.service.impl;

import com.senhua.my.wonderland.web.common.PageHelper;
import com.senhua.my.wonderland.web.entity.UserContent;
import com.senhua.my.wonderland.web.service.SolrService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/9/16.
 */
@Service
public class SolrServiceImpl implements SolrService {

    @Autowired
    HttpSolrClient solrClient;

    public PageHelper.Page<UserContent> findByKeyWords(String keyword, Integer pageNum, Integer pageSize) {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.setQuery("title:" + keyword);
        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");
        //分页
        if(pageNum == null || pageNum <1){
            pageNum = 1;
        }
        if(pageSize == null || pageSize < 1){
            pageSize = 7;
        }
        solrQuery.setStart((pageNum-1)*pageSize);
        solrQuery.setRows(pageSize);
        solrQuery.addSort("rpt_time",SolrQuery.ORDER.desc);
        //开始查询
        try {
            QueryResponse response = solrClient.query(solrQuery);
            //获取高亮数据集合
            Map<String ,Map<String,List<String>>> highlighting = response.getHighlighting();
            //获得结果集
            SolrDocumentList resultList = response.getResults();
            //获得总数量
            long totalNum = resultList.getNumFound();
            List<UserContent> list = new ArrayList<UserContent>();
            for(SolrDocument solrDocument:resultList){
                //创建文章对象
                UserContent content = new UserContent();
                //文章id
                String id = (String) solrDocument.get("id");
                Object contentl = solrDocument.get("content");
                Object commentNum = solrDocument.get("comment_num");
                Object downvote = solrDocument.get("downvote");
                Object upvote = solrDocument.get("upvote");
                Object nickName = solrDocument.get("nick_name");
                Object imgUrl = solrDocument.get("img_url");
                Object uid = solrDocument.get("u_id");
                Object rpt_time = solrDocument.get("rpt_time");
                Object category = solrDocument.get("category");
                Object personal = solrDocument.get("personal");
                //取得高亮数据集合中的文章标题
                Map<String,List<String>> map = highlighting.get(id);
                String title = map.get("title").get(0);

                content.setId(Long.parseLong(id));
                content.setCommentNum(Integer.parseInt((commentNum.toString())));
                content.setDownvote(Integer.parseInt(downvote.toString()));
                content.setUpvote(Integer.parseInt(upvote.toString()));
                content.setNickName(nickName.toString());
                content.setImgUrl(imgUrl.toString());
                content.setuId(Long.parseLong(uid.toString()));
                content.setTitle(title);
                content.setPersonal(personal.toString());
                Date date = (Date)rpt_time;
                content.setRptTime(date);
                List<String> clist = (ArrayList)contentl;
                content.setContent(clist.get(0).toString());
                content.setCategory(category.toString());
                list.add(content);
            }
            //开始分页
            PageHelper.startPage(pageNum,pageSize);
            //分页结束
            PageHelper.Page page = PageHelper.endPage();
            page.setResult(list);
            page.setTotal(totalNum);
            return page;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUserContent(UserContent userContent) {
        if(userContent != null){
            addDocument(userContent);
        }
    }

    public void updateUserContent(UserContent userContent) {
        if(userContent != null){
            addDocument(userContent);
        }
    }

    public void deleteById(Long id) {
        try {
            solrClient.deleteById(id.toString());
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDocument(UserContent userContent) {
        try {
            SolrInputDocument inputDocument = new SolrInputDocument();
            inputDocument.addField("comment_num", userContent.getCommentNum());
            inputDocument.addField("downvote",userContent.getDownvote());
            inputDocument.addField("upvote",userContent.getUpvote());
            inputDocument.addField("nick_name",userContent.getNickName());
            inputDocument.addField("img_url",userContent.getImgUrl());
            inputDocument.addField("rpt_time",userContent.getRptTime());
            inputDocument.addField("content",userContent.getContent());
            inputDocument.addField("category",userContent.getCategory());
            inputDocument.addField("title",userContent.getTitle());
            inputDocument.addField("u_id",userContent.getuId());
            inputDocument.addField("id",userContent.getId());
            inputDocument.addField("personal",userContent.getPersonal());
            solrClient.add(inputDocument);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
