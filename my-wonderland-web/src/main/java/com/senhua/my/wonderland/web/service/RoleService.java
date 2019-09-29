package com.senhua.my.wonderland.web.service;

import com.senhua.my.wonderland.web.entity.Role;

import java.util.List;

/**
 * Created by Administrator on 2019/9/17.
 */
public interface RoleService {

    /**
     * 根据用户id查询所有角色
     * @param uid
     * @return
     */
    List<Role> findByUid(Long uid);
}
