<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    String redirect = request.getParameter("redirect");
    if (redirect == null) redirect = "";
%>
<%@ include file="common/header.jsp" %>

<div class="container">
    <div class="row" style="margin-top:100px;">
        <div class="col-md-4 col-md-offset-4">
            <div class="panel form-panel">
                <div class="panel-heading">用户登录</div>
                <div class="panel-body" style="padding:30px;">
                    <% if (error != null) { %>
                    <div class="alert alert-danger"><%= error %></div>
                    <% } %>
                    <% if (success != null) { %>
                    <div class="alert alert-success"><%= success %></div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/user" method="post">
                        <input type="hidden" name="action" value="login">
                        <input type="hidden" name="redirect" value="<%= redirect %>">
                        <div class="form-group">
                            <label>用户名</label>
                            <input type="text" name="username" class="form-control" placeholder="请输入用户名" required>
                        </div>
                        <div class="form-group">
                            <label>密码</label>
                            <input type="password" name="password" class="form-control" placeholder="请输入密码" required>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block" style="margin-top:20px;">登 录</button>
                    </form>
                    <hr>
                    <p class="text-center">还没有账号？<a href="register.jsp" style="color:#e65100;">立即注册</a></p>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
