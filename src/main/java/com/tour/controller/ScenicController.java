package com.tour.controller;

import com.tour.model.City;
import com.tour.model.Announcement;
import com.tour.model.BrowseRecord;
import com.tour.model.Message;
import com.tour.model.Scenic;
import com.tour.model.User;
import com.tour.service.MessageService;
import com.tour.service.ScenicService;
import com.tour.dao.AnnouncementDao;
import com.tour.dao.BrowseRecordDao;
import com.tour.dao.MessageDao;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 景点控制器 — 处理景点列表/详情/搜索/留言
 */
public class ScenicController extends HttpServlet {

    private final ScenicService scenicService = new ScenicService();
    private final MessageService messageService = new MessageService();
    private final BrowseRecordDao browseRecordDao = new BrowseRecordDao();
    private final AnnouncementDao announcementDao = new AnnouncementDao();

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
            case "search":
                search(req, resp);
                break;
            case "doMessage":
                doMessage(req, resp);
                break;
            case "deleteMessage":
                deleteMessage(req, resp);
                break;
            default:
                list(req, resp);
                break;
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cityIdStr = req.getParameter("cityId");
        String keyword = req.getParameter("keyword");
        String pageStr = req.getParameter("page");

        Long cityId = null;
        if (cityIdStr != null && !cityIdStr.isEmpty()) {
            cityId = Long.parseLong(cityIdStr);
        }
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }
        int pageSize = 9;

        Map<String, Object> result = scenicService.listScenics(cityId, keyword, page, pageSize);
        List<City> cities = scenicService.getAllCities();

        req.setAttribute("scenics", result.get("list"));
        req.setAttribute("total", result.get("total"));
        req.setAttribute("page", result.get("page"));
        req.setAttribute("totalPages", result.get("totalPages"));
        req.setAttribute("cities", cities);
        req.setAttribute("currentCityId", cityId);
        req.setAttribute("currentKeyword", keyword);

        req.getRequestDispatcher("scenic/list.jsp").forward(req, resp);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String scenicIdStr = req.getParameter("id");
        if (scenicIdStr == null || scenicIdStr.isEmpty()) {
            resp.sendRedirect("scenic?action=list");
            return;
        }

        Long scenicId = Long.parseLong(scenicIdStr);
        Scenic scenic = scenicService.getDetail(scenicId);
        if (scenic == null) {
            resp.sendRedirect("scenic?action=list");
            return;
        }

        // 自动记录浏览历史
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            BrowseRecord br = new BrowseRecord();
            br.setUserId(user.getUserId());
            br.setBrowseType(1); // 1=景点
            br.setTargetId(scenicId);
            browseRecordDao.insert(br);
        }

        Map<String, Object> surroundings = scenicService.getSurroundings(scenicId);
        List<Message> messages = messageService.getMessages(scenicId);
        List<Announcement> announcements = announcementDao.findByScenicId(scenicId);

        req.setAttribute("scenic", scenic);
        req.setAttribute("foods", surroundings.get("foods"));
        req.setAttribute("hotels", surroundings.get("hotels"));
        req.setAttribute("messages", messages);
        req.setAttribute("announcements", announcements);
        req.getRequestDispatcher("scenic/detail.jsp").forward(req, resp);
    }

    private void doMessage(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String scenicIdStr = req.getParameter("scenicId");
        String content = req.getParameter("content");

        if (scenicIdStr == null || content == null || content.trim().isEmpty()) {
            resp.getWriter().write("<script>alert('留言不能为空');history.back();</script>");
            return;
        }

        Long scenicId = Long.parseLong(scenicIdStr);
        messageService.addMessage(scenicId, user.getUserId(), content);
        resp.sendRedirect("scenic?action=detail&id=" + scenicId);
    }

    private void deleteMessage(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Long roleId = (Long) session.getAttribute("roleId");

        String messageIdStr = req.getParameter("messageId");
        String scenicIdStr = req.getParameter("scenicId");
        if (messageIdStr != null) {
            Long msgId = Long.parseLong(messageIdStr);
            MessageDao messageDao = new MessageDao();
            Message msg = messageDao.findById(msgId);
            // 作者本人或管理员可删除
            if (msg != null && (roleId == 4 || msg.getUserId().equals(user.getUserId()))) {
                messageService.delete(msgId);
            }
        }
        resp.sendRedirect("scenic?action=detail&id=" + (scenicIdStr != null ? scenicIdStr : ""));
    }

    private void search(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        list(req, resp);
    }
}
