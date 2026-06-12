package com.tour.controller;

import com.tour.dao.AnnouncementDao;
import com.tour.dao.MessageDao;
import com.tour.dao.ScenicDao;
import com.tour.model.Announcement;
import com.tour.model.Message;
import com.tour.model.Scenic;
import com.tour.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * 景区管理员控制器 — 公告发布/留言管理（roleId=3）
 */
public class ScenicAdminController extends HttpServlet {

    private final AnnouncementDao announcementDao = new AnnouncementDao();
    private final MessageDao messageDao = new MessageDao();
    private final ScenicDao scenicDao = new ScenicDao();

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
        if (action == null) action = "panel";

        switch (action) {
            case "panel":
                panel(req, resp);
                break;
            case "manage":
                manage(req, resp);
                break;
            case "doAnnounce":
                doAnnounce(req, resp);
                break;
            case "deleteAnnounce":
                deleteAnnounce(req, resp);
                break;
            case "deleteMsg":
                deleteMsg(req, resp);
                break;
            default:
                panel(req, resp);
                break;
        }
    }

    private void panel(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Scenic> scenics = scenicDao.findAll(null, null, 1, 1000);
        req.setAttribute("scenics", scenics);
        req.getRequestDispatcher("scenic-admin/panel.jsp").forward(req, resp);
    }

    private void manage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String scenicIdStr = req.getParameter("scenicId");
        if (scenicIdStr == null || scenicIdStr.isEmpty()) {
            resp.sendRedirect("scenicAdmin?action=panel");
            return;
        }

        Long scenicId = Long.parseLong(scenicIdStr);
        Scenic scenic = scenicDao.findById(scenicId);

        List<Announcement> announcements = announcementDao.findByScenicId(scenicId);
        List<Message> messages = messageDao.findByScenicId(scenicId);

        req.setAttribute("scenic", scenic);
        req.setAttribute("announcements", announcements);
        req.setAttribute("messages", messages);
        req.getRequestDispatcher("scenic-admin/manage.jsp").forward(req, resp);
    }

    private void doAnnounce(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String scenicIdStr = req.getParameter("scenicId");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        if (scenicIdStr == null || title == null || content == null ||
                title.trim().isEmpty() || content.trim().isEmpty()) {
            resp.getWriter().write("<script>alert('标题和内容不能为空');history.back();</script>");
            return;
        }

        Announcement a = new Announcement();
        a.setScenicId(Long.parseLong(scenicIdStr));
        a.setUserId(user.getUserId());
        a.setTitle(title.trim());
        a.setContent(content.trim());
        boolean ok = announcementDao.insert(a);
        if (!ok) {
            resp.getWriter().write("<script>alert('发布失败，请检查tour_db数据库中announcements表是否存在');history.back();</script>");
            return;
        }

        resp.sendRedirect("scenicAdmin?action=manage&scenicId=" + scenicIdStr);
    }

    private void deleteAnnounce(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String annIdStr = req.getParameter("annId");
        String scenicIdStr = req.getParameter("scenicId");
        if (annIdStr != null) {
            announcementDao.delete(Long.parseLong(annIdStr));
        }
        resp.sendRedirect("scenicAdmin?action=manage&scenicId=" + (scenicIdStr != null ? scenicIdStr : ""));
    }

    private void deleteMsg(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String msgIdStr = req.getParameter("msgId");
        String scenicIdStr = req.getParameter("scenicId");
        if (msgIdStr != null) {
            messageDao.delete(Long.parseLong(msgIdStr));
        }
        resp.sendRedirect("scenicAdmin?action=manage&scenicId=" + (scenicIdStr != null ? scenicIdStr : ""));
    }
}
