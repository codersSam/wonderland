package com.senhua.my.wonderland.web.security.phone;

import com.senhua.my.wonderland.web.entity.Role;
import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.service.RoleService;
import com.senhua.my.wonderland.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


/**
 * Created by Administrator on 2019/9/19.
 */
public class PhoneUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    public UserDetails loadUserByUsername(String phone) {

        User user = userService.findByPhone(phone);
        if(user == null){
            throw  new PhoneNotFoundException("手机号码错误");

        }
        List<Role> roles = roleService.findByUid(user.getId());
        user.setRoles(roles);

        return user;
    }
}
