package com.tour.controller;

import com.tour.model.User;
import com.tour.service.CollectionService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * 收藏控制器
 */
public class CollectionController extends HttpServlet {

    private final CollectionService collectionService = new CollectionService();

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
            case "toggle":
                toggle(req, resp);
                break;
            default:
                list(req, resp);
                break;
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String typeStr = req.getParameter("type");
        String pageStr = req.getParameter("page");

        Integer collectType = null;
        if (typeStr != null && !typeStr.isEmpty()) {
            collectType = Integer.parseInt(typeStr);
        }
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        List<com.tour.model.Collection> collections = collectionService.listByUser(user.getUserId(), collectType, page, 10);
        int total = collectionService.countByUser(user.getUserId(), collectType);
        int totalPages = (int) Math.ceil((double) total / 10);

        req.setAttribute("collections", collections);
        req.setAttribute("total", total);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentType", collectType);
        req.getRequestDispatcher("collection/list.jsp").forward(req, resp);
    }

    private void toggle(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String typeStr = req.getParameter("type");
        String targetIdStr = req.getParameter("targetId");
        String redirect = req.getParameter("redirect");

        if (typeStr == null || targetIdStr == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        Integer collectType = Integer.parseInt(typeStr);
        Long targetId = Long.parseLong(targetIdStr);
        collectionService.toggle(user.getUserId(), collectType, targetId);

        if (redirect != null && !redirect.isEmpty()) {
            resp.sendRedirect(redirect);
        } else {
            resp.sendRedirect("collection?action=list");
        }
    }
}
