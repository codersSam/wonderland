package com.senhua.my.wonderland.web.service.impl;

import com.senhua.my.wonderland.web.dao.UserMapper;
import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2019/7/20.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public int regist(User user) {
        return userMapper.insert(user);
    }

    public User login(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return userMapper.selectOne(user);
    }

    public User findByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.selectOne(user);
    }

    public User findByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return userMapper.selectOne(user);
    }

    public User findById(Long id) {
        User user = new User();
        user.setId(id);
        return userMapper.selectOne(user);
    }

    @Transactional
    public void deleteByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        userMapper.delete(user);
    }

    @Transactional
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);

    }
}
