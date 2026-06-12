package com.tour.controller;

import com.tour.model.Trip;
import com.tour.model.User;
import com.tour.service.TripService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

/**
 * 行程控制器 — 列表/创建/编辑/删除
 */
public class TripController extends HttpServlet {

    private final TripService tripService = new TripService();

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
            case "create":
                createPage(req, resp);
                break;
            case "doCreate":
                doCreate(req, resp);
                break;
            case "edit":
                editPage(req, resp);
                break;
            case "doEdit":
                doEdit(req, resp);
                break;
            case "delete":
                delete(req, resp);
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

        String pageStr = req.getParameter("page");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        Map<String, Object> result = tripService.listByUser(user.getUserId(), page, 10);
        req.setAttribute("trips", result.get("list"));
        req.setAttribute("total", result.get("total"));
        req.setAttribute("page", result.get("page"));
        req.setAttribute("totalPages", result.get("totalPages"));

        req.getRequestDispatcher("trip/list.jsp").forward(req, resp);
    }

    private void createPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("scenics", tripService.getAllScenics());
        req.getRequestDispatcher("trip/create.jsp").forward(req, resp);
    }

    private void doCreate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String tripName = req.getParameter("tripName");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String tripContent = req.getParameter("tripContent");

        Long tripId = tripService.create(user.getUserId(), tripName, startDate, endDate, tripContent);
        if (tripId != null) {
            resp.sendRedirect("trip?action=list");
        } else {
            resp.getWriter().write("<script>alert('创建失败，请检查日期格式');history.back();</script>");
        }
    }

    private void editPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect("trip?action=list");
            return;
        }

        Long tripId = Long.parseLong(idStr);
        Trip trip = tripService.getDetail(tripId);
        if (trip == null) {
            resp.sendRedirect("trip?action=list");
            return;
        }

        req.setAttribute("trip", trip);
        req.setAttribute("scenics", tripService.getAllScenics());
        req.getRequestDispatcher("trip/edit.jsp").forward(req, resp);
    }

    private void doEdit(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idStr = req.getParameter("tripId");
        String tripName = req.getParameter("tripName");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String tripContent = req.getParameter("tripContent");

        if (idStr == null) {
            resp.sendRedirect("trip?action=list");
            return;
        }

        boolean ok = tripService.update(Long.parseLong(idStr), tripName, startDate, endDate, tripContent);
        if (ok) {
            resp.sendRedirect("trip?action=list");
        } else {
            resp.getWriter().write("<script>alert('更新失败');history.back();</script>");
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null) {
            tripService.delete(Long.parseLong(idStr));
        }
        resp.sendRedirect("trip?action=list");
    }
}
