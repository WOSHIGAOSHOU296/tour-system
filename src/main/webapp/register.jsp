<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
%>
<%@ include file="common/header.jsp" %>

<div class="container">
    <div class="row" style="margin-top:60px;">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel form-panel">
                <div class="panel-heading">用户注册</div>
                <div class="panel-body" style="padding:30px;">
                    <% if (error != null) { %>
                    <div class="alert alert-danger"><%= error %></div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/user" method="post" onsubmit="return checkForm()">
                        <input type="hidden" name="action" value="register">
                        <div class="form-group">
                            <label>用户名 <span class="text-danger">*</span></label>
                            <input type="text" name="username" id="username" class="form-control"
                                   placeholder="请输入登录用户名" required>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>密码 <span class="text-danger">*</span></label>
                                    <input type="password" name="password" id="password" class="form-control"
                                           placeholder="至少6位密码" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>确认密码 <span class="text-danger">*</span></label>
                                    <input type="password" name="confirmPassword" id="confirmPassword"
                                           class="form-control" placeholder="再次输入密码" required>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>角色</label>
                            <select name="roleId" class="form-control">
                                <option value="2">普通用户</option>
                                <option value="3">景区管理员</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>昵称</label>
                            <input type="text" name="nickname" class="form-control" placeholder="显示名称，默认使用用户名">
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>邮箱</label>
                                    <input type="email" name="email" class="form-control" placeholder="用于找回密码">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>手机号</label>
                                    <input type="text" name="phone" class="form-control" placeholder="联系电话">
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block" style="margin-top:10px;">注 册</button>
                    </form>
                    <hr>
                    <p class="text-center">已有账号？<a href="login.jsp" style="color:#e65100;">立即登录</a></p>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function checkForm() {
    var pwd = document.getElementById('password').value;
    var cfm = document.getElementById('confirmPassword').value;
    if (pwd.length < 6) { alert('密码长度不能少于6位'); return false; }
    if (pwd !== cfm) { alert('两次输入的密码不一致'); return false; }
    return true;
}
</script>

<%@ include file="common/footer.jsp" %>
