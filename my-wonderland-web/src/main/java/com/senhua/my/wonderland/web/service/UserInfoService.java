package com.senhua.my.wonderland.web.service;

import com.senhua.my.wonderland.web.entity.UserInfo;

/**
 * Created by Administrator on 2019/9/11.
 */
public interface UserInfoService {
    /**
     * 根据用户id查找用户详细信息
     * @param id
     * @return
     */
    UserInfo findByUid(Long id);

    /**
     * 增加用户详细信息
     * @param userInfo
     */
    void add(UserInfo userInfo);

    /**
     * 修改用户详细信息
     * @param userInfo
     */
    void update(UserInfo userInfo);
}
