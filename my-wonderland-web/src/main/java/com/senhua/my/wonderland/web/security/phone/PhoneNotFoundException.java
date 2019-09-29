package com.senhua.my.wonderland.web.security.phone;


import org.springframework.security.core.AuthenticationException;

/**
 * Created by Administrator on 2019/9/19.
 */
public class PhoneNotFoundException extends AuthenticationException {

    public PhoneNotFoundException(String msg,Throwable t){
        super(msg,t);
    }

    public PhoneNotFoundException(String msg){
        super(msg);
    }
}
