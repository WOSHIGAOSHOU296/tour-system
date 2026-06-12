<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.BrowseRecord" %>
<%
    List<BrowseRecord> records = (List<BrowseRecord>) request.getAttribute("records");
    int total = (Integer) request.getAttribute("total");
    int currentPage = (Integer) request.getAttribute("page");
    int totalPages = (Integer) request.getAttribute("totalPages");
    String[] typeNames = {"", "景点", "攻略", "帖子"};
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#128065; 浏览历史</h2>
    </div>

    <p class="text-muted">共 <strong style="color:#e65100;"><%= total %></strong> 条记录</p>

    <% if (records != null && !records.isEmpty()) {
        for (BrowseRecord r : records) {
            String link = "#";
            if (r.getBrowseType() == 1) link = request.getContextPath() + "/scenic?action=detail&id=" + r.getTargetId();
            else if (r.getBrowseType() == 2) link = request.getContextPath() + "/strategy?action=detail&id=" + r.getTargetId();
            else if (r.getBrowseType() == 3) link = request.getContextPath() + "/forum?action=detail&id=" + r.getTargetId();
    %>
    <div class="panel post-panel">
        <div class="panel-body">
            <span class="card-tag tag-price"><%= typeNames[r.getBrowseType()] %></span>
            &nbsp; ID: <%= r.getTargetId() %>
            <small class="text-muted pull-right">&#128338; <%= r.getBrowseTime() %></small>
            <a href="<%= link %>" class="btn btn-primary btn-xs pull-right" style="margin-right:10px;">查看</a>
        </div>
    </div>
    <% }} else { %>
    <div class="alert alert-info text-center" style="padding:40px;border-radius:16px;">暂无浏览记录</div>
    <% } %>

    <% if (totalPages > 1) { %>
    <nav class="text-center">
        <ul class="pagination">
            <% if (currentPage > 1) { %>
            <li><a href="user?action=history&page=<%= currentPage - 1 %>">&laquo;</a></li>
            <% } %>
            <% for (int i = 1; i <= totalPages; i++) { %>
            <li <%= i == currentPage ? "class=\"active\"" : "" %>>
                <a href="user?action=history&page=<%= i %>"><%= i %></a>
            </li>
            <% } %>
            <% if (currentPage < totalPages) { %>
            <li><a href="user?action=history&page=<%= currentPage + 1 %>">&raquo;</a></li>
            <% } %>
        </ul>
    </nav>
    <% } %>
</div>

<%@ include file="../common/footer.jsp" %>
