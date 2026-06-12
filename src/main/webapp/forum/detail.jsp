<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tour.model.ForumPost" %>
<%@ page import="com.tour.model.PostComment" %>
<%@ page import="java.util.List" %>
<%
    ForumPost post = (ForumPost) request.getAttribute("post");
    List<PostComment> comments = (List<PostComment>) request.getAttribute("comments");
    if (post == null) {
        response.sendRedirect(request.getContextPath() + "/forum?action=list");
        return;
    }
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <ol class="breadcrumb">
        <li><a href="${pageContext.request.contextPath}/index.jsp">首页</a></li>
        <li><a href="forum?action=list">旅途论坛</a></li>
        <li class="active">帖子详情</li>
    </ol>

    <div class="row">
        <div class="col-md-9">
            <!-- 帖子内容 -->
            <div class="info-panel panel">
                <div class="panel-heading"><%= post.getTitle() %></div>
                <div class="panel-body" style="padding:24px;">
                    <p class="text-muted small">
                        &#128100; <%= post.getUsername() %> &nbsp;&nbsp;
                        &#127991; <span class="card-tag tag-price"><%= post.getCategory() %></span> &nbsp;&nbsp;
                        &#128338; <%= post.getCreatedAt() %>
                    </p>
                    <hr>
                    <p style="white-space:pre-wrap;line-height:1.8;font-size:15px;"><%= post.getContent() %></p>
                </div>
            </div>

            <!-- 评论区 -->
            <div class="section-panel panel panel-default" style="margin-top:24px;">
                <div class="panel-heading" style="background:#fff3e0;color:#bf360c;">
                    &#128221; 评论 (<%= comments != null ? comments.size() : 0 %>)
                </div>
                <div class="panel-body">
                    <% if (comments != null && !comments.isEmpty()) {
                        for (PostComment c : comments) { %>
                    <div class="comment-box">
                        <div>
                            <strong style="color:#e65100;"><%= c.getUsername() %></strong>
                            <small class="text-muted"> &middot; <%= c.getCreatedAt() %></small>
                            <% if (roleId != null && roleId == 4) { %>
                            <a href="forum?action=deleteComment&commentId=<%= c.getCommentId() %>&postId=<%= post.getPostId() %>"
                               class="text-danger pull-right" onclick="return confirm('确定删除该评论？')"
                               style="font-size:12px;">删除</a>
                            <% } %>
                        </div>
                        <p style="margin-top:6px;"><%= c.getContent() %></p>
                    </div>
                    <% }} else { %>
                    <p class="text-muted">暂无评论，快来发表第一条评论吧</p>
                    <% } %>
                </div>
            </div>

            <!-- 发表评论 -->
            <% if (sessionUser != null) { %>
            <div class="section-panel panel panel-default" style="margin-top:20px;">
                <div class="panel-heading" style="background:#fdf6f0;">发表评论</div>
                <div class="panel-body">
                    <form action="forum" method="post">
                        <input type="hidden" name="action" value="doComment">
                        <input type="hidden" name="postId" value="<%= post.getPostId() %>">
                        <div class="form-group">
                            <textarea name="content" class="form-control" rows="3"
                                      placeholder="写下你的评论..." required
                                      style="border-radius:12px;"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">发表评论</button>
                    </form>
                </div>
            </div>
            <% } else { %>
            <div class="alert alert-info" style="margin-top:20px;padding:16px;border-radius:12px;">
                请先 <a href="${pageContext.request.contextPath}/login.jsp?redirect=forum?action=detail&id=<%= post.getPostId() %>" style="color:#e65100;">登录</a> 后发表评论
            </div>
            <% } %>
        </div>

        <div class="col-md-3">
            <a href="collection?action=toggle&type=3&targetId=<%= post.getPostId() %>&redirect=forum?action=detail&id=<%= post.getPostId() %>"
               class="btn btn-default btn-block">&#11088; 收藏</a>
            <a href="forum?action=list" class="btn btn-default btn-block" style="margin-top:6px;">返回列表</a>
            <% if (sessionUser != null) { %>
            <a href="forum?action=create" class="btn btn-primary btn-block" style="margin-top:10px;">发布新帖</a>
            <% } %>
            <% if (roleId != null && roleId == 4) { %>
            <hr>
            <div class="section-panel panel" style="border-top:4px solid #c62828;">
                <div class="panel-heading" style="background:#fce4ec;color:#c62828;">管理员操作</div>
                <div class="panel-body">
                    <% if (post.getStatus() == 1) { %>
                    <button class="btn btn-default btn-block btn-sm" disabled>下架此帖（开发中）</button>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
