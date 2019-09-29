package com.senhua.my.wonderland.web.security.account;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2019/9/18.
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    private String errorPage;

    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        boolean isAjax = "XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"));
        if(isAjax){
            String jsonObject = "{\"message\":\"Access is denied!\",\"access-denied\":true}";
            String contentType = "application/json";
            httpServletResponse.setContentType(contentType);
            PrintWriter out = httpServletResponse.getWriter();
            out.print(jsonObject);
            out.flush();
            out.close();
        }else{
            if(!httpServletResponse.isCommitted()){
                if(this.errorPage != null){
                    httpServletRequest.setAttribute("SPRING_SECRITY_403_EXCEPTION",e);
                    httpServletResponse.setStatus(403);
                    RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher(this.errorPage);
                    dispatcher.forward(httpServletRequest,httpServletResponse);
                }else{
                    httpServletResponse.sendError(403,e.getMessage());
                }
            }
        }
    }

    public void setErrorPage(String errorPage){
        if(errorPage != null && !errorPage.startsWith("/")){
            throw new IllegalArgumentException("errorPage must begin with '/'");
        }else{
            this.errorPage = errorPage;
        }
    }
}
