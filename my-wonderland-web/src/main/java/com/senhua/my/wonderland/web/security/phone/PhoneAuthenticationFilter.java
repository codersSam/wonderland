package com.senhua.my.wonderland.web.security.phone;

import com.senhua.my.wonderland.web.common.CodeValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2019/9/19.
 */
public class PhoneAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String phoneParameter = "telephone";
    public static final String codeParameter = "phone_code";

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    private HttpServletRequest request;
    private HttpServletResponse response;


    protected PhoneAuthenticationFilter() {
        super(new AntPathRequestMatcher("/phoneLogin"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException {
        String phone = this.obtainPhone(request);
        String phone_code = this.obtainValidateCode(request);
        if(phone == null){
            phone = "";
        }
        if(phone_code == null){
            phone_code = "";
        }
        phone = phone.trim();
        String cache_code = redisTemplate.opsForValue().get(phone);
        boolean flag = CodeValidate.validateCode(phone_code,cache_code);
        if(!flag){
            throw new PhoneNotFoundException("手机验证码错误");
        }
        PhoneAuthenticationToken authenticationToken = new PhoneAuthenticationToken(phone);
        this.setDetails(request,authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    protected void setDetails(HttpServletRequest request, PhoneAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    protected String obtainValidateCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }

    protected String obtainPhone(HttpServletRequest request) {

        return  request.getParameter(phoneParameter);

    }

}
