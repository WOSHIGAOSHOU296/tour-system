<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.ForumPost" %>
<%
    List<ForumPost> posts = (List<ForumPost>) request.getAttribute("posts");
    int total = (Integer) request.getAttribute("total");
    int currentPage = (Integer) request.getAttribute("page");
    int totalPages = (Integer) request.getAttribute("totalPages");
    String currentCategory = (String) request.getAttribute("currentCategory");
    if (currentCategory == null) currentCategory = "";
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#128172; 旅途论坛</h2>
    </div>

    <div class="row">
        <div class="col-md-9">
            <!-- 分类标签 -->
            <div class="cat-group">
                <a href="forum?action=list" class="btn <%= "".equals(currentCategory) ? "active" : "" %>">全部</a>
                <a href="forum?action=list&category=攻略" class="btn <%= "攻略".equals(currentCategory) ? "active" : "" %>">攻略</a>
                <a href="forum?action=list&category=结伴" class="btn <%= "结伴".equals(currentCategory) ? "active" : "" %>">结伴</a>
                <a href="forum?action=list&category=问答" class="btn <%= "问答".equals(currentCategory) ? "active" : "" %>">问答</a>
            </div>

            <p class="text-muted">共 <strong style="color:#e65100;"><%= total %></strong> 篇帖子</p>

            <% if (posts != null && !posts.isEmpty()) {
                for (ForumPost p : posts) { %>
            <div class="panel post-panel">
                <div class="panel-body">
                    <h4>
                        <a href="forum?action=detail&id=<%= p.getPostId() %>"><%= p.getTitle() %></a>
                        <span class="card-tag tag-price pull-right"><%= p.getCategory() %></span>
                    </h4>
                    <p class="text-muted small">
                        &#128100; <%= p.getUsername() %> &nbsp;&nbsp;
                        &#128338; <%= p.getCreatedAt() %>
                    </p>
                    <p style="line-height:1.7;"><%= p.getContent() != null && p.getContent().length() > 80 ?
                            p.getContent().substring(0, 80) + "..." : p.getContent() %></p>
                    <% if (sessionUser != null && (roleId != null && roleId == 4 || p.getUserId().equals(sessionUser.getUserId()))) { %>
                    <a href="forum?action=deletePost&id=<%= p.getPostId() %>" class="text-danger small"
                       onclick="return confirm('确定删除该帖子？')">删除</a>
                    <% } %>
                </div>
            </div>
            <% }} else { %>
            <div class="alert alert-info text-center" style="padding:40px;border-radius:16px;">暂无帖子，快来发布第一篇吧</div>
            <% } %>

            <!-- 分页 -->
            <% if (totalPages > 1) { %>
            <nav class="text-center">
                <ul class="pagination">
                    <% if (currentPage > 1) { %>
                    <li><a href="forum?action=list&category=<%= currentCategory %>&page=<%= currentPage - 1 %>">&laquo;</a></li>
                    <% } %>
                    <% for (int i = 1; i <= totalPages; i++) { %>
                    <li <%= i == currentPage ? "class=\"active\"" : "" %>>
                        <a href="forum?action=list&category=<%= currentCategory %>&page=<%= i %>"><%= i %></a>
                    </li>
                    <% } %>
                    <% if (currentPage < totalPages) { %>
                    <li><a href="forum?action=list&category=<%= currentCategory %>&page=<%= currentPage + 1 %>">&raquo;</a></li>
                    <% } %>
                </ul>
            </nav>
            <% } %>
        </div>

        <div class="col-md-3">
            <% if (sessionUser != null) { %>
            <a href="forum?action=create" class="btn btn-primary btn-block" style="padding:12px;">
                &#9997; 发布新帖
            </a>
            <% } else { %>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-default btn-block" style="border-radius:24px;">登录后发帖</a>
            <% } %>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
