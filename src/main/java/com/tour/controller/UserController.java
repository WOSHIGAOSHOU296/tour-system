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
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.login(username, password);
        if (user == null) {
            req.setAttribute("error", "用户名或密码错误，或账号已被禁用");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        Long roleId = userService.getUserRoleId(user.getUserId());
        session.setAttribute("roleId", roleId);

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

    private void history(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        String pageStr = req.getParameter("page");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        List<BrowseRecord> records = browseRecordDao.findByUser(sessionUser.getUserId(), page, 10);
        int total = browseRecordDao.countByUser(sessionUser.getUserId());
        int totalPages = (int) Math.ceil((double) total / 10);

        req.setAttribute("records", records);
        req.setAttribute("total", total);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("WEB-INF/history.jsp").forward(req, resp);
    }
}
