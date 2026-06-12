package com.tour.controller;

import com.tour.dao.ForumPostDao;
import com.tour.dao.UserDao;
import com.tour.model.ForumPost;
import com.tour.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理控制器 — 平台管理员专用（roleId=4）
 */
public class AdminController extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final ForumPostDao forumPostDao = new ForumPostDao();

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
        if (action == null) action = "dashboard";

        switch (action) {
            case "dashboard":
                dashboard(req, resp);
                break;
            case "users":
                users(req, resp);
                break;
            case "disableUser":
                disableUser(req, resp);
                break;
            case "changeRole":
                changeRole(req, resp);
                break;
            case "posts":
                posts(req, resp);
                break;
            case "togglePost":
                togglePost(req, resp);
                break;
            default:
                dashboard(req, resp);
                break;
        }
    }

    private void dashboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userCount = userDao.countAll();
        int postCount = forumPostDao.countAll(null);
        int totalViews = getTotalViews();

        req.setAttribute("userCount", userCount);
        req.setAttribute("postCount", postCount);
        req.setAttribute("totalViews", totalViews);
        req.getRequestDispatcher("admin/dashboard.jsp").forward(req, resp);
    }

    private void users(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pageStr = req.getParameter("page");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        List<User> users = userDao.findAll(page, 10);
        int total = userDao.countAll();
        int totalPages = (int) Math.ceil((double) total / 10);

        // 构建 userId → roleId 映射
        Map<Long, Long> roleMap = new HashMap<>();
        for (User u : users) {
            Long roleId = userDao.findRoleIdByUserId(u.getUserId());
            if (roleId != null) roleMap.put(u.getUserId(), roleId);
        }

        req.setAttribute("users", users);
        req.setAttribute("total", total);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("roleMap", roleMap);
        req.getRequestDispatcher("admin/users.jsp").forward(req, resp);
    }

    private void disableUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String userIdStr = req.getParameter("userId");
        String statusStr = req.getParameter("status");
        if (userIdStr != null && statusStr != null) {
            userDao.updateStatus(Long.parseLong(userIdStr), Integer.parseInt(statusStr));
        }
        resp.sendRedirect("admin?action=users");
    }

    private void changeRole(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String userIdStr = req.getParameter("userId");
        String roleIdStr = req.getParameter("roleId");
        if (userIdStr != null && roleIdStr != null) {
            userDao.updateRole(Long.parseLong(userIdStr), Long.parseLong(roleIdStr));
        }
        resp.sendRedirect("admin?action=users");
    }

    private void posts(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String category = req.getParameter("category");
        String keyword = req.getParameter("keyword");
        String pageStr = req.getParameter("page");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        List<ForumPost> posts = forumPostDao.findAllForAdmin(category, keyword, page, 10);
        int total = forumPostDao.countAll(category);
        int totalPages = (int) Math.ceil((double) total / 10);

        req.setAttribute("posts", posts);
        req.setAttribute("total", total);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentCategory", category != null ? category : "");
        req.setAttribute("currentKeyword", keyword != null ? keyword : "");
        req.getRequestDispatcher("admin/posts.jsp").forward(req, resp);
    }

    private void togglePost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String postIdStr = req.getParameter("postId");
        String statusStr = req.getParameter("status");
        if (postIdStr != null && statusStr != null) {
            forumPostDao.updateStatus(Long.parseLong(postIdStr), Integer.parseInt(statusStr));
        }
        resp.sendRedirect("admin?action=posts");
    }

    private int getTotalViews() {
        // 景点总浏览量
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        java.sql.ResultSet rs = null;
        try {
            conn = com.tour.util.DBUtil.getConnection();
            stmt = conn.prepareStatement("SELECT COALESCE(SUM(view_count), 0) FROM attractions");
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            com.tour.util.DBUtil.closeAll(rs, stmt, conn);
        }
        return 0;
    }
}
