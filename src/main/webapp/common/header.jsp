<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tour.model.User" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    Long roleId = (Long) session.getAttribute("roleId");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个性化旅游定制平台</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.4.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.4.1/dist/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
</head>
<body>
<nav class="navbar nav-orange navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar">
                <span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">
                &#127758; 旅行定制
            </a>
        </div>
        <div class="collapse navbar-collapse" id="navbar">
            <ul class="nav navbar-nav">
                <li><a href="${pageContext.request.contextPath}/index.jsp">首页</a></li>
                <li><a href="${pageContext.request.contextPath}/scenic?action=list">景点浏览</a></li>
                <li><a href="${pageContext.request.contextPath}/forum?action=list">旅途论坛</a></li>
                <li><a href="${pageContext.request.contextPath}/trip?action=list">行程规划</a></li>
                <li><a href="${pageContext.request.contextPath}/strategy?action=list">旅友圈</a></li>
                <% if (roleId != null && roleId == 3) { %>
                <li><a href="${pageContext.request.contextPath}/scenicAdmin?action=panel">景区管理</a></li>
                <% } %>
                <% if (roleId != null && roleId == 4) { %>
                <li><a href="${pageContext.request.contextPath}/scenicAdmin?action=panel">景区管理</a></li>
                <li><a href="${pageContext.request.contextPath}/admin?action=dashboard">后台管理</a></li>
                <% } %>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <% if (sessionUser == null) { %>
                <li><a href="${pageContext.request.contextPath}/login.jsp" class="nav-btn-outline">登录</a></li>
                <li><a href="${pageContext.request.contextPath}/register.jsp" class="nav-btn-fill">注册</a></li>
                <% } else { %>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" style="padding-top:10px;padding-bottom:10px;">
                        &#128100; <%= sessionUser.getNickname() != null ? sessionUser.getNickname() : sessionUser.getUsername() %>
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/user?action=profile">个人中心</a></li>
                        <li><a href="${pageContext.request.contextPath}/collection?action=list">我的收藏</a></li>
                        <li><a href="${pageContext.request.contextPath}/user?action=history">浏览历史</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="${pageContext.request.contextPath}/user?action=logout">退出登录</a></li>
                    </ul>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>
<div class="main-content">
