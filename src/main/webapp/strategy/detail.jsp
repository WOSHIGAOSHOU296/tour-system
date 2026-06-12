<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tour.model.Strategy" %>
<%
    Strategy strategy = (Strategy) request.getAttribute("strategy");
    if (strategy == null) {
        response.sendRedirect(request.getContextPath() + "/strategy?action=list");
        return;
    }
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <ol class="breadcrumb">
        <li><a href="${pageContext.request.contextPath}/index.jsp">首页</a></li>
        <li><a href="strategy?action=list">旅友圈</a></li>
        <li class="active">攻略详情</li>
    </ol>

    <div class="row">
        <div class="col-md-8">
            <div class="info-panel panel">
                <div class="panel-heading"><%= strategy.getTitle() %></div>
                <div class="panel-body" style="padding:24px;">
                    <p class="text-muted small">
                        &#128100; <%= strategy.getUsername() %> &nbsp;&nbsp;
                        &#127963; <span class="card-tag tag-score"><%= strategy.getScenicName() %></span> &nbsp;&nbsp;
                        &#128338; <%= strategy.getCreatedAt() %>
                    </p>
                    <hr>
                    <p style="white-space:pre-wrap;line-height:2;font-size:15px;"><%= strategy.getContent() %></p>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <a href="strategy?action=list" class="btn btn-default btn-block">返回列表</a>
            <% if (sessionUser != null) { %>
            <a href="strategy?action=create" class="btn btn-primary btn-block" style="margin-top:10px;">发布新攻略</a>
            <% } %>
            <% if (roleId != null && roleId == 4) { %>
            <hr>
            <a href="strategy?action=delete&id=<%= strategy.getStrategyId() %>"
               class="btn btn-default btn-block" style="color:#c62828;"
               onclick="return confirm('确定删除该攻略？')">删除此攻略</a>
            <% } %>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
