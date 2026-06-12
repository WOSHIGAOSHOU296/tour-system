<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    int userCount = (Integer) request.getAttribute("userCount");
    int postCount = (Integer) request.getAttribute("postCount");
    int totalViews = (Integer) request.getAttribute("totalViews");
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#128736; 后台管理 · 仪表盘</h2>
    </div>

    <!-- 统计卡片 -->
    <div class="row" style="margin-bottom:30px;">
        <div class="col-md-4">
            <div class="panel" style="border-radius:16px;border-top:4px solid #e65100;text-align:center;padding:30px;background:#fff;box-shadow:0 2px 12px rgba(0,0,0,0.06);">
                <div style="font-size:48px;color:#e65100;font-weight:bold;"><%= userCount %></div>
                <div style="color:#8d6e63;margin-top:8px;">&#128101; 注册用户</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel" style="border-radius:16px;border-top:4px solid #ff8f00;text-align:center;padding:30px;background:#fff;box-shadow:0 2px 12px rgba(0,0,0,0.06);">
                <div style="font-size:48px;color:#ff8f00;font-weight:bold;"><%= postCount %></div>
                <div style="color:#8d6e63;margin-top:8px;">&#128221; 论坛帖子</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel" style="border-radius:16px;border-top:4px solid #bf360c;text-align:center;padding:30px;background:#fff;box-shadow:0 2px 12px rgba(0,0,0,0.06);">
                <div style="font-size:48px;color:#bf360c;font-weight:bold;"><%= totalViews %></div>
                <div style="color:#8d6e63;margin-top:8px;">&#128065; 景点总浏览量</div>
            </div>
        </div>
    </div>

    <!-- 快捷入口 -->
    <div class="row">
        <div class="col-md-4">
            <a href="admin?action=users" class="btn btn-primary btn-block" style="padding:16px;font-size:16px;">
                &#128101; 用户管理
            </a>
        </div>
        <div class="col-md-4">
            <a href="admin?action=posts" class="btn btn-primary btn-block" style="padding:16px;font-size:16px;">
                &#128221; 帖子审核
            </a>
        </div>
        <div class="col-md-4">
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-default btn-block" style="padding:16px;font-size:16px;">
                &#127968; 返回首页
            </a>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
