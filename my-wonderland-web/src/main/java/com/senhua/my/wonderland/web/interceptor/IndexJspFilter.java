package com.senhua.my.wonderland.web.interceptor;

import com.senhua.my.wonderland.web.common.PageHelper;
import com.senhua.my.wonderland.web.controller.BaseController;
import com.senhua.my.wonderland.web.dao.UserContentMapper;
import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.entity.UserContent;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2019/7/26.
 */

/**
 * 在进入 index.jsp 之前进行过滤器拦截，先获取页面数据，之后再返回 index.jsp 页面。
 */
public class IndexJspFilter extends BaseController implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("==========自定义过滤器==========");
        User user = getCurrentUser();
        ServletContext servletContext = servletRequest.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        //filter 初始化时，注解的 bean 还没初始化，加 @Autowired 注解不会起作用，所以通过 ApplicationContext 手动获取 UserContentMapper 对象。
        UserContentMapper userContentMapper = ctx.getBean(UserContentMapper.class);

        //在开始分页和结束分页之间查询数据，PageHelper 会对分页开始之后的第一个查询语句进行分页，封装在 Page 对象中。
        PageHelper.startPage(null,null);
        Example e = new Example(UserContent.class);
        e.setOrderByClause("rpt_time DESC");
        List<UserContent> list = userContentMapper.findByJoin(null);
        PageHelper.Page endPage = PageHelper.endPage();
        servletRequest.setAttribute("user",user);
        servletRequest.setAttribute("page",endPage);

        filterChain.doFilter(servletRequest,servletResponse);
    }

    public void destroy() {

    }
}
