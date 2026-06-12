<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.ForumPost" %>
<%
    List<ForumPost> posts = (List<ForumPost>) request.getAttribute("posts");
    int total = (Integer) request.getAttribute("total");
    int currentPage = (Integer) request.getAttribute("page");
    int totalPages = (Integer) request.getAttribute("totalPages");
    String currentCategory = (String) request.getAttribute("currentCategory");
    String currentKeyword = (String) request.getAttribute("currentKeyword");
    if (currentCategory == null) currentCategory = "";
    if (currentKeyword == null) currentKeyword = "";
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <ol class="breadcrumb">
        <li><a href="admin?action=dashboard">后台管理</a></li>
        <li class="active">帖子审核</li>
    </ol>

    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#128221; 帖子审核</h2>
    </div>

    <!-- 筛选 -->
    <div class="well" style="margin-bottom:20px;">
        <form action="admin" method="get" class="form-inline">
            <input type="hidden" name="action" value="posts">
            <div class="form-group" style="margin-right:8px;">
                <select name="category" class="form-control">
                    <option value="">全部分类</option>
                    <option value="攻略" <%= "攻略".equals(currentCategory) ? "selected" : "" %>>攻略</option>
                    <option value="结伴" <%= "结伴".equals(currentCategory) ? "selected" : "" %>>结伴</option>
                    <option value="问答" <%= "问答".equals(currentCategory) ? "selected" : "" %>>问答</option>
                </select>
            </div>
            <div class="form-group" style="margin-right:8px;">
                <input type="text" name="keyword" class="form-control" placeholder="搜索帖子..."
                       value="<%= currentKeyword %>" style="width:200px;">
            </div>
            <button type="submit" class="btn btn-primary">&#128269; 筛选</button>
        </form>
    </div>

    <p class="text-muted">共 <strong style="color:#e65100;"><%= total %></strong> 篇帖子</p>

    <% if (posts != null && !posts.isEmpty()) {
        for (ForumPost p : posts) { %>
    <div class="panel post-panel">
        <div class="panel-body">
            <h4>
                <%= p.getTitle() %>
                <span class="pull-right">
                    <% if (p.getStatus() == 1) { %>
                    <span class="card-tag tag-free">正常</span>
                    <% } else { %>
                    <span class="card-tag tag-views">已下架</span>
                    <% } %>
                    <span class="card-tag tag-score"><%= p.getCategory() %></span>
                </span>
            </h4>
            <p class="text-muted small">
                &#128100; <%= p.getUsername() %> &nbsp;&nbsp; &#128338; <%= p.getCreatedAt() %>
            </p>
            <p><%= p.getContent() != null && p.getContent().length() > 100 ?
                    p.getContent().substring(0, 100) + "..." : p.getContent() %></p>
            <div style="margin-top:10px;">
                <% if (p.getStatus() == 1) { %>
                <a href="admin?action=togglePost&postId=<%= p.getPostId() %>&status=0"
                   class="btn btn-default btn-sm" style="color:#c62828;"
                   onclick="return confirm('确定下架该帖子？')">下架</a>
                <% } else { %>
                <a href="admin?action=togglePost&postId=<%= p.getPostId() %>&status=1"
                   class="btn btn-default btn-sm" style="color:#2e7d32;"
                   onclick="return confirm('确定上架该帖子？')">恢复上架</a>
                <% } %>
            </div>
        </div>
    </div>
    <% }} else { %>
    <div class="alert alert-info text-center" style="padding:40px;border-radius:16px;">暂无帖子</div>
    <% } %>

    <!-- 分页 -->
    <% if (totalPages > 1) { %>
    <nav class="text-center">
        <ul class="pagination">
            <% if (currentPage > 1) { %>
            <li><a href="admin?action=posts&category=<%= currentCategory %>&keyword=<%= currentKeyword %>&page=<%= currentPage - 1 %>">&laquo;</a></li>
            <% } %>
            <% for (int i = 1; i <= totalPages; i++) { %>
            <li <%= i == currentPage ? "class=\"active\"" : "" %>>
                <a href="admin?action=posts&category=<%= currentCategory %>&keyword=<%= currentKeyword %>&page=<%= i %>"><%= i %></a>
            </li>
            <% } %>
            <% if (currentPage < totalPages) { %>
            <li><a href="admin?action=posts&category=<%= currentCategory %>&keyword=<%= currentKeyword %>&page=<%= currentPage + 1 %>">&raquo;</a></li>
            <% } %>
        </ul>
    </nav>
    <% } %>

    <a href="admin?action=dashboard" class="btn btn-default">返回仪表盘</a>
</div>

<%@ include file="../common/footer.jsp" %>
