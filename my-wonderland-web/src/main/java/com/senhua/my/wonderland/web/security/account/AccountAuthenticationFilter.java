package com.senhua.my.wonderland.web.security.account;

import com.senhua.my.wonderland.web.common.CodeValidate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/9/18.
 */
public class AccountAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String codeParameter = "code";

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);
        String code = this.obtainCode(request);
        String caChecode = (String) request.getSession().getAttribute("VERCODE_KEY");
        boolean flag = CodeValidate.validateCode(code,caChecode);
        if(!flag){
            throw new UsernameNotFoundException("验证码错误！");
        }
        if(username == null){
            username = "";
        }
        if(password == null){
            password = "";
        }
        username = username.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,password);
        this.setDetails(request,authRequest);
        Authentication authenticate = this.getAuthenticationManager().authenticate(authRequest);
        return authenticate;
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(this.codeParameter);
    }

}
