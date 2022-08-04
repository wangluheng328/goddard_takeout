package com.wes.goddard.filter;

import com.alibaba.fastjson.JSON;
import com.wes.goddard.common.BaseContext;
import com.wes.goddard.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Check if user is already logged in
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter{
    /*
    * AntPathMatcher is provided by Spring
    * It can match a path with an array of paths
    * it allows using symbol such as * and ? for matching representations
    *       - ？: 1 character
    *       - * : 0 or more characters
    *       - ** : 0 or more directories/characters
    * Here it is used in the check method, which is used in the doFilter method to check if the url does not need to be handled*/
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. get url for this request
        String requestURI = request.getRequestURI();// i.e. /backend/index.html

        log.info("Request intercepted：{}",requestURI);

        // set urls that don't need to be handled
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**"
        };


        //2. determine whether this request needs to be handled
        boolean check = check(urls, requestURI);

        //3. if not, return
        if(check){
            log.info("This request {} doesn't need to be handled",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4. check whether logged in, if so, return
        /**
         * Here BaseContext.setCurrentId is to work with the MyMetaObjectHandler class, so that the create/update time/user can be
         * easily retrieve from meta data. It uses thread. Similarly, this line of code can be placed in the Employee handler in the
         * methods update and save. However, although it still works, it loses its meanings such that it is equivalent as not using meta
         * data. The point of this approach is to save the time, so that each time we define a new entity which contains CRUD operations
         * that need these meta data, it can simply retrieve from BaseContext
         */
        if(request.getSession().getAttribute("employee") != null){

            long empId = (Long) request.getSession().getAttribute("employee");
            log.info("User already logged in，id：{}",empId);

            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("User not logged in");
        //5. If not logged in
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
