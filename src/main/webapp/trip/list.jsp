<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Trip" %>
<%
    List<Trip> trips = (List<Trip>) request.getAttribute("trips");
    int total = (Integer) request.getAttribute("total");
    int currentPage = (Integer) request.getAttribute("page");
    int totalPages = (Integer) request.getAttribute("totalPages");
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#128197; 行程规划</h2>
    </div>

    <div class="row">
        <div class="col-md-9">
            <p class="text-muted">共 <strong style="color:#e65100;"><%= total %></strong> 个行程</p>

            <% if (trips != null && !trips.isEmpty()) {
                for (Trip t : trips) { %>
            <div class="panel post-panel">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-8">
                            <h4 style="margin-top:0;">
                                <a href="trip?action=edit&id=<%= t.getTripId() %>"><%= t.getTripName() %></a>
                            </h4>
                            <p class="text-muted small">
                                &#128197; <%= t.getStartDate() %> ~ <%= t.getEndDate() %>
                                &nbsp;&nbsp;&#128338; 创建于 <%= t.getCreatedAt() %>
                            </p>
                            <p style="line-height:1.7;white-space:pre-wrap;"><%= t.getTripContent() != null && t.getTripContent().length() > 150 ?
                                    t.getTripContent().substring(0, 150) + "..." : t.getTripContent() %></p>
                        </div>
                        <div class="col-md-4 text-right" style="padding-top:10px;">
                            <a href="trip?action=edit&id=<%= t.getTripId() %>" class="btn btn-default btn-sm">&#9998; 编辑</a>
                            <a href="trip?action=delete&id=<%= t.getTripId() %>" class="btn btn-default btn-sm"
                               onclick="return confirm('确定删除该行程？')" style="color:#c62828;">删除</a>
                        </div>
                    </div>
                </div>
            </div>
            <% }} else { %>
            <div class="alert alert-info text-center" style="padding:40px;border-radius:16px;">
                暂无行程，快来规划你的第一次旅行吧
            </div>
            <% } %>

            <!-- 分页 -->
            <% if (totalPages > 1) { %>
            <nav class="text-center">
                <ul class="pagination">
                    <% if (currentPage > 1) { %>
                    <li><a href="trip?action=list&page=<%= currentPage - 1 %>">&laquo;</a></li>
                    <% } %>
                    <% for (int i = 1; i <= totalPages; i++) { %>
                    <li <%= i == currentPage ? "class=\"active\"" : "" %>>
                        <a href="trip?action=list&page=<%= i %>"><%= i %></a>
                    </li>
                    <% } %>
                    <% if (currentPage < totalPages) { %>
                    <li><a href="trip?action=list&page=<%= currentPage + 1 %>">&raquo;</a></li>
                    <% } %>
                </ul>
            </nav>
            <% } %>
        </div>

        <div class="col-md-3">
            <a href="trip?action=create" class="btn btn-primary btn-block" style="padding:12px;">
                &#9997; 创建新行程
            </a>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
