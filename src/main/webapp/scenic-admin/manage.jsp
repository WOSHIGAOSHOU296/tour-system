<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Scenic" %>
<%@ page import="com.tour.model.Announcement" %>
<%@ page import="com.tour.model.Message" %>
<%
    Scenic scenic = (Scenic) request.getAttribute("scenic");
    List<Announcement> announcements = (List<Announcement>) request.getAttribute("announcements");
    List<Message> messages = (List<Message>) request.getAttribute("messages");
    if (scenic == null) {
        response.sendRedirect(request.getContextPath() + "/scenicAdmin?action=panel");
        return;
    }
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <ol class="breadcrumb">
        <li><a href="scenicAdmin?action=panel">景区管理中心</a></li>
        <li class="active"><%= scenic.getScenicName() %></li>
    </ol>

    <div class="row">
        <div class="col-md-8">
            <!-- 公告列表 -->
            <div class="section-panel panel" style="border-top:4px solid #e65100;">
                <div class="panel-heading" style="background:#fbe9e7;color:#bf360c;">
                    &#128226; 景区公告 (<%= announcements != null ? announcements.size() : 0 %>)
                </div>
                <div class="panel-body">
                    <% if (announcements != null && !announcements.isEmpty()) {
                        for (Announcement a : announcements) { %>
                    <div class="comment-box" style="border-left-color:#e65100;">
                        <div>
                            <strong style="color:#bf360c;"><%= a.getTitle() %></strong>
                            <small class="text-muted"> &middot; <%= a.getUsername() %> &middot; <%= a.getCreatedAt() %></small>
                            <a href="scenicAdmin?action=deleteAnnounce&annId=<%= a.getAnnId() %>&scenicId=<%= scenic.getScenicId() %>"
                               class="text-danger pull-right" onclick="return confirm('确定删除该公告？')"
                               style="font-size:12px;">删除</a>
                        </div>
                        <p style="margin-top:6px;"><%= a.getContent() %></p>
                    </div>
                    <% }} else { %>
                    <p class="text-muted">暂无公告</p>
                    <% } %>
                </div>
            </div>

            <!-- 留言管理 -->
            <div class="section-panel panel" style="border-top:4px solid #ff8f00;margin-top:20px;">
                <div class="panel-heading" style="background:#fff8e1;color:#e65100;">
                    &#128221; 留言管理 (<%= messages != null ? messages.size() : 0 %>)
                </div>
                <div class="panel-body">
                    <% if (messages != null && !messages.isEmpty()) {
                        for (Message m : messages) { %>
                    <div class="comment-box">
                        <div>
                            <strong style="color:#e65100;"><%= m.getUsername() %></strong>
                            <small class="text-muted"> &middot; <%= m.getCreatedAt() %></small>
                            <a href="scenicAdmin?action=deleteMsg&msgId=<%= m.getMessageId() %>&scenicId=<%= scenic.getScenicId() %>"
                               class="text-danger pull-right" onclick="return confirm('确定删除该留言？')"
                               style="font-size:12px;">删除</a>
                        </div>
                        <p style="margin-top:6px;"><%= m.getContent() %></p>
                    </div>
                    <% }} else { %>
                    <p class="text-muted">暂无留言</p>
                    <% } %>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <!-- 景区信息 -->
            <div class="info-panel panel">
                <div class="panel-heading"><%= scenic.getScenicName() %></div>
                <div class="panel-body" style="padding:0;">
                    <table class="table table-striped" style="margin:0;">
                        <tr><th>城市</th><td><%= scenic.getCityName() %></td></tr>
                        <tr><th>评分</th><td><span style="color:#ff8f00;">&#9733;</span> <%= scenic.getAverageScore() %>分</td></tr>
                        <tr><th>浏览量</th><td><strong style="color:#e65100;"><%= scenic.getViewCount() %></strong>次</td></tr>
                    </table>
                </div>
            </div>

            <!-- 发布公告 -->
            <div class="panel form-panel" style="margin-top:16px;">
                <div class="panel-heading">发布公告</div>
                <div class="panel-body" style="padding:20px;">
                    <form action="scenicAdmin" method="post">
                        <input type="hidden" name="action" value="doAnnounce">
                        <input type="hidden" name="scenicId" value="<%= scenic.getScenicId() %>">
                        <div class="form-group">
                            <input type="text" name="title" class="form-control"
                                   placeholder="公告标题" maxlength="50" required>
                        </div>
                        <div class="form-group">
                            <textarea name="content" class="form-control" rows="4"
                                      placeholder="公告内容（门票政策、开放时间调整、特色活动等）"
                                      maxlength="500" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">发布公告</button>
                    </form>
                </div>
            </div>

            <a href="scenicAdmin?action=panel" class="btn btn-default btn-block" style="margin-top:10px;">返回景区列表</a>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
