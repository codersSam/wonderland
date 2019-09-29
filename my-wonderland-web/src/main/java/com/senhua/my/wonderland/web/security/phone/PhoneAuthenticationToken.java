package com.senhua.my.wonderland.web.security.phone;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Administrator on 2019/9/19.
 */
public class PhoneAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public PhoneAuthenticationToken(Object principal){
        super((Collection) null);
        this.principal = principal;
        this.setAuthenticated(false);
    }

    public PhoneAuthenticationToken(Object principal,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException{
        if(isAuthenticated){
            throw new  IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }else{
            super.setAuthenticated(false);
        }
    }
}
