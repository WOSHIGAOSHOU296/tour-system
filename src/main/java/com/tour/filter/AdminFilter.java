package com.tour.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 管理员过滤器 — 拦截 /admin/*，仅平台管理员 (roleId=4) 可访问
 */
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Long roleId = (Long) session.getAttribute("roleId");
        if (roleId == null || roleId != 4) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<script>alert('无权限访问');history.back();</script>");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
