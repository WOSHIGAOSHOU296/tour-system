package com.tour.filter;

import com.tour.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录过滤器 — 拦截需要登录的页面
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // 未登录，重定向到登录页，附带原始URL
            String requestURI = req.getRequestURI();
            String queryString = req.getQueryString();
            String redirect = requestURI;
            if (queryString != null) {
                redirect += "?" + queryString;
            }
            resp.sendRedirect(req.getContextPath() + "/login.jsp?redirect=" +
                    java.net.URLEncoder.encode(redirect, "UTF-8"));
            return;
        }

        // 检查用户状态
        User user = (User) session.getAttribute("user");
        if (user.getStatus() != null && user.getStatus() == 0) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=账号已被禁用");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
