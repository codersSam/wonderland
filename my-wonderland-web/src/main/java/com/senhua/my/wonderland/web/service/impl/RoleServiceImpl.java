package com.senhua.my.wonderland.web.service.impl;

import com.senhua.my.wonderland.web.dao.RoleMapper;
import com.senhua.my.wonderland.web.entity.Role;
import com.senhua.my.wonderland.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2019/9/18.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public List<Role> findByUid(Long uid) {
        return roleMapper.findByUid(uid);
    }
}
