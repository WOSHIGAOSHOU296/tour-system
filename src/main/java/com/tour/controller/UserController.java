package com.tour.controller;

import com.tour.model.User;
import com.tour.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 用户控制器 — 处理注册/登录/注销/个人信息
 */
public class UserController extends HttpServlet {

    private final UserService userService = new UserService();

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

        String action = req.getParameter("action");
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
            default:
                resp.sendRedirect("index.jsp");
                break;
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.login(username, password);
        if (user == null) {
            req.setAttribute("error", "用户名或密码错误，或账号已被禁用");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        // 存入 Session
        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        Long roleId = userService.getUserRoleId(user.getUserId());
        session.setAttribute("roleId", roleId);

        // 跳转到来源页或首页
        String redirect = req.getParameter("redirect");
        if (redirect != null && !redirect.isEmpty()) {
            resp.sendRedirect(redirect);
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

        String error = userService.register(username, password, nickname, email, phone);
        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
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
        User sessionUser = (User) session.getAttribute("user");
        User user = userService.getProfile(sessionUser.getUserId());
        req.setAttribute("profile", user);
        req.getRequestDispatcher("WEB-INF/profile.jsp").forward(req, resp);
    }
}
