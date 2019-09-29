package com.senhua.my.wonderland.web.service.impl;

import com.senhua.my.wonderland.web.dao.RoleUserMapper;
import com.senhua.my.wonderland.web.entity.RoleUser;
import com.senhua.my.wonderland.web.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/9/24.
 */
@Service
public class RoleUserServiceImpl implements RoleUserService {

    @Autowired
    private RoleUserMapper roleUserMapper;

    public int add(RoleUser roleUser) {
        return roleUserMapper.insert(roleUser);
    }
}
