package com.tour.controller;

import com.tour.dao.BrowseRecordDao;
import com.tour.model.BrowseRecord;
import com.tour.model.User;
import com.tour.service.ForumService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

/**
 * 论坛控制器 — 处理帖子列表/详情/发帖/评论
 */
public class ForumController extends HttpServlet {

    private final ForumService forumService = new ForumService();
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
        if (action == null) action = "list";

        switch (action) {
            case "list":
                list(req, resp);
                break;
            case "detail":
                detail(req, resp);
                break;
            case "create":
                create(req, resp);
                break;
            case "doCreate":
                doCreate(req, resp);
                break;
            case "doComment":
                doComment(req, resp);
                break;
            case "deleteComment":
                deleteComment(req, resp);
                break;
            default:
                list(req, resp);
                break;
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String category = req.getParameter("category");
        String pageStr = req.getParameter("page");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        Map<String, Object> result = forumService.listPosts(category, page, 10);
        req.setAttribute("posts", result.get("list"));
        req.setAttribute("total", result.get("total"));
        req.setAttribute("page", result.get("page"));
        req.setAttribute("totalPages", result.get("totalPages"));
        req.setAttribute("currentCategory", category);

        req.getRequestDispatcher("forum/list.jsp").forward(req, resp);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String postIdStr = req.getParameter("id");
        if (postIdStr == null || postIdStr.isEmpty()) {
            resp.sendRedirect("forum?action=list");
            return;
        }

        Long postId = Long.parseLong(postIdStr);
        Map<String, Object> result = forumService.getPostDetail(postId);
        if (result == null) {
            resp.sendRedirect("forum?action=list");
            return;
        }

        req.setAttribute("post", result.get("post"));
        req.setAttribute("comments", result.get("comments"));

        // 自动记录浏览
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            BrowseRecord br = new BrowseRecord();
            br.setUserId(user.getUserId());
            br.setBrowseType(3); // 3=帖子
            br.setTargetId(postId);
            browseRecordDao.insert(br);
        }

        req.getRequestDispatcher("forum/detail.jsp").forward(req, resp);
    }

    private void create(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("forum/create.jsp").forward(req, resp);
    }

    private void doCreate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String category = req.getParameter("category");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        boolean ok = forumService.createPost(user.getUserId(), category, title, content);
        if (ok) {
            resp.sendRedirect("forum?action=list");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<script>alert('发布失败，标题和内容不能为空');history.back();</script>");
        }
    }

    private void doComment(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String postIdStr = req.getParameter("postId");
        String content = req.getParameter("content");

        if (postIdStr == null || content == null || content.trim().isEmpty()) {
            resp.getWriter().write("<script>alert('评论不能为空');history.back();</script>");
            return;
        }

        Long postId = Long.parseLong(postIdStr);
        forumService.addComment(postId, user.getUserId(), content);
        resp.sendRedirect("forum?action=detail&id=" + postId);
    }

    private void deleteComment(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Long roleId = (Long) session.getAttribute("roleId");

        String commentIdStr = req.getParameter("commentId");
        String postIdStr = req.getParameter("postId");

        if (commentIdStr != null && (roleId != null && roleId == 4)) {
            forumService.deleteComment(Long.parseLong(commentIdStr));
        }

        if (postIdStr != null) {
            resp.sendRedirect("forum?action=detail&id=" + postIdStr);
        } else {
            resp.sendRedirect("forum?action=list");
        }
    }
}
