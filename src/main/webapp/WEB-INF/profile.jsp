<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tour.model.User" %>
<%
    User profile = (User) request.getAttribute("profile");
    if (profile == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#128100; 个人中心</h2>
    </div>
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="info-panel panel">
                <div class="panel-heading">个人信息</div>
                <div class="panel-body" style="padding:0;">
                    <table class="table table-striped" style="margin:0;">
                        <tr><th>用户名</th><td><%= profile.getUsername() %></td></tr>
                        <tr><th>昵称</th><td><%= profile.getNickname() %></td></tr>
                        <tr><th>邮箱</th><td><%= profile.getEmail() %></td></tr>
                        <tr><th>手机号</th><td><%= profile.getPhone() %></td></tr>
                        <tr><th>性别</th><td>
                            <%= profile.getGender() == 1 ? "男" : (profile.getGender() == 2 ? "女" : "未知") %>
                        </td></tr>
                        <tr><th>注册时间</th><td><%= profile.getCreatedAt() %></td></tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
