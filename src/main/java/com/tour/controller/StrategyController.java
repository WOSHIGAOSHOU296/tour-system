package com.tour.controller;

import com.tour.dao.BrowseRecordDao;
import com.tour.model.BrowseRecord;
import com.tour.model.Scenic;
import com.tour.model.User;
import com.tour.service.StrategyService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 旅友圈控制器 — 攻略列表/详情/发布/删除
 */
public class StrategyController extends HttpServlet {

    private final StrategyService strategyService = new StrategyService();
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
                createPage(req, resp);
                break;
            case "doCreate":
                doCreate(req, resp);
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
        String scenicIdStr = req.getParameter("scenicId");
        String pageStr = req.getParameter("page");

        Long scenicId = null;
        if (scenicIdStr != null && !scenicIdStr.isEmpty()) {
            scenicId = Long.parseLong(scenicIdStr);
        }
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        Map<String, Object> result = strategyService.list(scenicId, page, 10);
        req.setAttribute("strategies", result.get("list"));
        req.setAttribute("total", result.get("total"));
        req.setAttribute("page", result.get("page"));
        req.setAttribute("totalPages", result.get("totalPages"));
        req.setAttribute("currentScenicId", scenicId);

        req.getRequestDispatcher("strategy/list.jsp").forward(req, resp);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect("strategy?action=list");
            return;
        }

        Long strategyId = Long.parseLong(idStr);
        com.tour.model.Strategy strategy = strategyService.getDetail(strategyId);
        if (strategy == null) {
            resp.sendRedirect("strategy?action=list");
            return;
        }

        req.setAttribute("strategy", strategy);

        // 自动记录浏览
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            BrowseRecord br = new BrowseRecord();
            br.setUserId(user.getUserId());
            br.setBrowseType(2); // 2=攻略
            br.setTargetId(strategyId);
            browseRecordDao.insert(br);
        }

        req.getRequestDispatcher("strategy/detail.jsp").forward(req, resp);
    }

    private void createPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Scenic> scenics = strategyService.getAllScenics();
        req.setAttribute("scenics", scenics);
        req.getRequestDispatcher("strategy/create.jsp").forward(req, resp);
    }

    private void doCreate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String scenicIdStr = req.getParameter("scenicId");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        if (scenicIdStr == null || title == null || content == null ||
                title.trim().isEmpty() || content.trim().isEmpty()) {
            resp.getWriter().write("<script>alert('请填写完整信息');history.back();</script>");
            return;
        }

        Long scenicId = Long.parseLong(scenicIdStr);
        boolean ok = strategyService.create(user.getUserId(), scenicId, title, content);
        if (ok) {
            resp.sendRedirect("strategy?action=list");
        } else {
            resp.getWriter().write("<script>alert('发布失败');history.back();</script>");
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        Long roleId = (Long) session.getAttribute("roleId");
        User user = (User) session.getAttribute("user");

        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect("strategy?action=list");
            return;
        }

        Long strategyId = Long.parseLong(idStr);
        com.tour.model.Strategy s = strategyService.getDetail(strategyId);
        // 作者本人或管理员可删除
        if (s != null && (roleId == 4 || s.getUserId().equals(user.getUserId()))) {
            strategyService.delete(strategyId);
        }
        resp.sendRedirect("strategy?action=list");
    }
}
