package com.senhua.my.wonderland.web.service.impl;

import com.senhua.my.wonderland.web.dao.OpenUserMapper;
import com.senhua.my.wonderland.web.entity.OpenUser;
import com.senhua.my.wonderland.web.service.OpenUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2019/9/16.
 */
@Service
public class OpenUserServiceImpl implements OpenUserService {

    @Autowired
    private OpenUserMapper openUserMapper;

    public OpenUser findByOpenId(String openId) {
        OpenUser openUser = new OpenUser();
        openUser.setOpenId(openId);
        return openUserMapper.selectOne(openUser);
    }

    public void add(OpenUser openUser) {
        openUserMapper.insert(openUser);
    }

    public void update(OpenUser openUser) {
        openUserMapper.updateByPrimaryKeySelective(openUser);
    }

    public List<OpenUser> findByUid(Long uid) {
        OpenUser openUser = new OpenUser();
        openUser.setuId(uid);
        return openUserMapper.select(openUser);
    }

    public void deleteByUidAndType(Long uid, String type) {
        OpenUser openUser = new OpenUser();
        openUser.setuId(uid);
        openUser.setOpenType(type);
        openUserMapper.delete(openUser);
    }
}
