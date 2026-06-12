package com.tour.controller;

import com.tour.dao.BrowseRecordDao;
import com.tour.model.BrowseRecord;
import com.tour.model.User;
import com.tour.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * 用户控制器 — 处理注册/登录/注销/个人信息/浏览历史
 */
public class UserController extends HttpServlet {

    private final UserService userService = new UserService();

    /**
     * 其他功能（注册、登录、个人信息）都经过了UserService，是因为它们属于"用户域"的业务逻辑——涉及密码加密、状态校验、角色分配等有业务规则的操作，需要 Service 层来协调。
     * 但浏览历史查询是纯数据展示，没有业务逻辑：它不需要校验、不需要转换、不需要协调多个 DAO。所以为了快速实现，直接调了 DAO。
     */
    private final BrowseRecordDao browseRecordDao = new BrowseRecordDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = req.getParameter("action");// 获取http请求参数中的action字段，用于区分不同的操作
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "login":
                login(req, resp);
                break;
            case "register":
                register(req, resp);
                break;
            case "logout":
                logout(req, resp);
                break;
            case "profile":
                profile(req, resp);
                break;
            case "history":
                history(req, resp);
                break;
            default:
                resp.sendRedirect("index.jsp");
                break;
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");// 获取http请求参数中的username和password字段，用于登录验证
        String password = req.getParameter("password");

        User user = userService.login(username, password);
        if (user == null) {
            req.setAttribute("error", "用户名或密码错误，或账号已被禁用");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();//获取HTTP会话对象，用于存储用户信息，实现权限控制和个性化功能。
        session.setAttribute("user", user);//将用户对象存入HTTP会话中，用于后续的权限控制和个性化功能。
        Long roleId = userService.getUserRoleId(user.getUserId());
        session.setAttribute("roleId", roleId);

        String redirect = req.getParameter("redirect");//获取http请求参数中的redirect字段，用于指定登录成功后跳转的页面。
        if (redirect != null && !redirect.isEmpty()) {
            resp.sendRedirect(redirect);//跳转到指定页面，登录成功后的跳转。如果redirect为空或未指定，则默认重定向到首页index.jsp。
        } else {
            resp.sendRedirect("index.jsp");
        }
    }

    private void register(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        String error = userService.register(username, password, nickname, email, phone);//调用UserService的register方法执行注册，返回错误信息（如果有的话）
        if (error != null) { //判断是否有错误：如果error不为null，说明注册失败
            req.setAttribute("error", error);//将错误信息存入request作用域
            req.getRequestDispatcher("register.jsp").forward(req, resp);//转发回注册页面显示错误提示
            return;//终止后续执行
        }

        req.setAttribute("success", "注册成功，请登录");
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect("index.jsp");
    }

    private void profile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        User sessionUser = (User) session.getAttribute("user");//从HTTP会话中读取之前存储的用户对象，并强制转换为User类型。用于验证用户是否已登录，获取当前操作用户的身份信息，实现权限控制和个性化功能。
        User user = userService.getProfile(sessionUser.getUserId());
        req.setAttribute("profile", user);
        req.getRequestDispatcher("WEB-INF/profile.jsp").forward(req, resp);
    }

    private void history(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");//从HTTP会话中读取之前存储的用户对象，并强制转换为User类型。用于验证用户是否已登录，获取当前操作用户，实现权限控制和个性化功能。
        String pageStr = req.getParameter("page");//获取http请求参数中的page字段，用于指定当前页码。
        int page = 1;//默认当前页码为1，表示第一页。如果请求中包含page参数且不为空，则将当前页码设置为该值。
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        List<BrowseRecord> records = browseRecordDao.findByUser(sessionUser.getUserId(), page, 10);
        int total = browseRecordDao.countByUser(sessionUser.getUserId());//获取用户浏览记录的总数，用于计算总页数。
        int totalPages = (int) Math.ceil((double) total / 10);

        req.setAttribute("records", records);
        req.setAttribute("total", total);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("WEB-INF/history.jsp").forward(req, resp);//转发回浏览记录页面，并传入用户浏览记录列表、总记录数、当前页码和总页数等信息。
    }
}
