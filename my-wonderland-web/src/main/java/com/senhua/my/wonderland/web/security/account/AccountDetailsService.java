package com.senhua.my.wonderland.web.security.account;

import com.senhua.my.wonderland.web.entity.Role;
import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.service.RoleService;
import com.senhua.my.wonderland.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2019/9/18.
 */
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByEmail(s);
        if(user == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        List<Role> roles = roleService.findByUid(user.getId());
        user.setRoles(roles);
        return user;
    }
}
