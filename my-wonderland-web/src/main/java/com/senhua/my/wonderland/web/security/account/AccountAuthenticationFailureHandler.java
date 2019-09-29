package com.senhua.my.wonderland.web.security.account;

import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2019/9/19.
 */
public class AccountAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private String defaultFailureUrl;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        User user = userService.findByEmail(username);
        if(user != null){
            if("0".equals(user.getState())){
                request.setAttribute("error","active");
            }else{
                request.setAttribute("error","fail");
            }
        }else{
            request.setAttribute("error","fail");
        }

        request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
    }

    public String getDefaultFailureUrl() {
        return defaultFailureUrl;
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        this.defaultFailureUrl = defaultFailureUrl;
    }
}
