<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Collection" %>
<%
    List<Collection> collections = (List<Collection>) request.getAttribute("collections");
    int total = (Integer) request.getAttribute("total");
    int currentPage = (Integer) request.getAttribute("page");
    int totalPages = (Integer) request.getAttribute("totalPages");
    Integer currentType = (Integer) request.getAttribute("currentType");

    String[] typeNames = {"", "景点", "攻略", "帖子", "行程"};
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#11088; 我的收藏</h2>
    </div>

    <div class="cat-group">
        <a href="collection?action=list" class="btn <%= currentType == null ? "active" : "" %>">全部</a>
        <a href="collection?action=list&type=1" class="btn <%= currentType != null && currentType == 1 ? "active" : "" %>">景点</a>
        <a href="collection?action=list&type=2" class="btn <%= currentType != null && currentType == 2 ? "active" : "" %>">攻略</a>
        <a href="collection?action=list&type=3" class="btn <%= currentType != null && currentType == 3 ? "active" : "" %>">帖子</a>
        <a href="collection?action=list&type=4" class="btn <%= currentType != null && currentType == 4 ? "active" : "" %>">行程</a>
    </div>

    <p class="text-muted">共 <strong style="color:#e65100;"><%= total %></strong> 条收藏</p>

    <% if (collections != null && !collections.isEmpty()) {
        for (Collection c : collections) {
            String link = "#";
            if (c.getCollectType() == 1) link = "scenic?action=detail&id=" + c.getTargetId();
            else if (c.getCollectType() == 2) link = "strategy?action=detail&id=" + c.getTargetId();
            else if (c.getCollectType() == 3) link = "forum?action=detail&id=" + c.getTargetId();
            else if (c.getCollectType() == 4) link = "trip?action=edit&id=" + c.getTargetId();
    %>
    <div class="panel post-panel">
        <div class="panel-body">
            <div class="row">
                <div class="col-md-8">
                    <h4 style="margin-top:0;">
                        <span class="card-tag tag-price"><%= typeNames[c.getCollectType()] %></span>
                        &nbsp; ID: <%= c.getTargetId() %>
                    </h4>
                    <p class="text-muted small">&#128338; <%= c.getCreatedAt() %></p>
                </div>
                <div class="col-md-4 text-right" style="padding-top:6px;">
                    <a href="<%= link %>" class="btn btn-primary btn-sm">查看</a>
                    <a href="collection?action=toggle&type=<%= c.getCollectType() %>&targetId=<%= c.getTargetId() %>&redirect=collection?action=list"
                       class="btn btn-default btn-sm" style="color:#c62828;">取消收藏</a>
                </div>
            </div>
        </div>
    </div>
    <% }} else { %>
    <div class="alert alert-info text-center" style="padding:40px;border-radius:16px;">暂无收藏</div>
    <% } %>

    <% if (totalPages > 1) { %>
    <nav class="text-center">
        <ul class="pagination">
            <% if (currentPage > 1) { %>
            <li><a href="collection?action=list&type=<%= currentType != null ? currentType : "" %>&page=<%= currentPage - 1 %>">&laquo;</a></li>
            <% } %>
            <% for (int i = 1; i <= totalPages; i++) { %>
            <li <%= i == currentPage ? "class=\"active\"" : "" %>>
                <a href="collection?action=list&type=<%= currentType != null ? currentType : "" %>&page=<%= i %>"><%= i %></a>
            </li>
            <% } %>
            <% if (currentPage < totalPages) { %>
            <li><a href="collection?action=list&type=<%= currentType != null ? currentType : "" %>&page=<%= currentPage + 1 %>">&raquo;</a></li>
            <% } %>
        </ul>
    </nav>
    <% } %>
</div>

<%@ include file="../common/footer.jsp" %>
