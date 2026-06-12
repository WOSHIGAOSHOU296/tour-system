<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Strategy" %>
<%
    List<Strategy> strategies = (List<Strategy>) request.getAttribute("strategies");
    int total = (Integer) request.getAttribute("total");
    int currentPage = (Integer) request.getAttribute("page");
    int totalPages = (Integer) request.getAttribute("totalPages");
    Long currentScenicId = (Long) request.getAttribute("currentScenicId");
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#127748; 旅友圈 · 景点攻略</h2>
    </div>

    <div class="row">
        <div class="col-md-9">
            <p class="text-muted">共 <strong style="color:#e65100;"><%= total %></strong> 篇攻略</p>

            <% if (strategies != null && !strategies.isEmpty()) {
                for (Strategy s : strategies) { %>
            <div class="panel post-panel">
                <div class="panel-body">
                    <h4>
                        <a href="strategy?action=detail&id=<%= s.getStrategyId() %>"><%= s.getTitle() %></a>
                        <span class="card-tag tag-score pull-right"><%= s.getScenicName() %></span>
                    </h4>
                    <p class="text-muted small">
                        &#128100; <%= s.getUsername() %> &nbsp;&nbsp;
                        &#128338; <%= s.getCreatedAt() %>
                    </p>
                    <p style="line-height:1.7;"><%= s.getContent() != null && s.getContent().length() > 100 ?
                            s.getContent().substring(0, 100) + "..." : s.getContent() %></p>
                </div>
            </div>
            <% }} else { %>
            <div class="alert alert-info text-center" style="padding:40px;border-radius:16px;">暂无攻略，快来分享你的旅行经验吧</div>
            <% } %>

            <!-- 分页 -->
            <% if (totalPages > 1) { %>
            <nav class="text-center">
                <ul class="pagination">
                    <% if (currentPage > 1) { %>
                    <li><a href="strategy?action=list&scenicId=<%= currentScenicId != null ? currentScenicId : "" %>&page=<%= currentPage - 1 %>">&laquo;</a></li>
                    <% } %>
                    <% for (int i = 1; i <= totalPages; i++) { %>
                    <li <%= i == currentPage ? "class=\"active\"" : "" %>>
                        <a href="strategy?action=list&scenicId=<%= currentScenicId != null ? currentScenicId : "" %>&page=<%= i %>"><%= i %></a>
                    </li>
                    <% } %>
                    <% if (currentPage < totalPages) { %>
                    <li><a href="strategy?action=list&scenicId=<%= currentScenicId != null ? currentScenicId : "" %>&page=<%= currentPage + 1 %>">&raquo;</a></li>
                    <% } %>
                </ul>
            </nav>
            <% } %>
        </div>

        <div class="col-md-3">
            <% if (sessionUser != null) { %>
            <a href="strategy?action=create" class="btn btn-primary btn-block" style="padding:12px;">
                &#9997; 发布攻略
            </a>
            <% } else { %>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-default btn-block" style="border-radius:24px;">登录后发布</a>
            <% } %>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
