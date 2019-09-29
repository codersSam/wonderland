package com.senhua.my.wonderland.web.service;

import com.senhua.my.wonderland.web.entity.OpenUser;

import java.util.List;

/**
 * Created by Administrator on 2019/9/16.
 */
public interface OpenUserService {
    /**
     * 根据openId查找OpenUser
     * @param openId
     * @return
     */
    OpenUser findByOpenId(String openId);

    /**
     * 添加openUser
     * @param openUser
     */
    void add(OpenUser openUser);

    /**
     * 修改openUser信息
     * @param openUser
     */
    void update(OpenUser openUser);

    /**
     * 根据用户id查询OpenUser集合
     * @param uid
     * @return
     */
    List<OpenUser> findByUid(Long uid);

    /**
     * 根据用户id第三方类型删除OpenUser
     * @param uid
     * @param type
     * @return
     */
    void deleteByUidAndType(Long uid,String type);
}
