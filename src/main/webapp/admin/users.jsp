<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.tour.model.User" %>
<%
    List<User> users = (List<User>) request.getAttribute("users");
    Map<Long, Long> roleMap = (Map<Long, Long>) request.getAttribute("roleMap");
    int total = (Integer) request.getAttribute("total");
    int currentPage = (Integer) request.getAttribute("page");
    int totalPages = (Integer) request.getAttribute("totalPages");
    String[] roleNames = {"", "游客", "普通用户", "景区管理员", "平台管理员"};
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <ol class="breadcrumb">
        <li><a href="admin?action=dashboard">后台管理</a></li>
        <li class="active">用户管理</li>
    </ol>

    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#128101; 用户管理</h2>
    </div>

    <p class="text-muted">共 <strong style="color:#e65100;"><%= total %></strong> 名用户</p>

    <div class="table-responsive">
        <table class="table table-striped" style="background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 2px 12px rgba(0,0,0,0.06);">
            <thead style="background:linear-gradient(90deg,#e65100,#ff8f00);color:#fff;">
                <tr>
                    <th>ID</th><th>用户名</th><th>昵称</th><th>角色</th><th>状态</th><th>注册时间</th><th>操作</th>
                </tr>
            </thead>
            <tbody>
                <% if (users != null && !users.isEmpty()) {
                    for (User u : users) { %>
                <tr>
                    <td><%= u.getUserId() %></td>
                    <td><%= u.getUsername() %></td>
                    <td><%= u.getNickname() %></td>
                    <td>
                        <form action="admin" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="changeRole">
                            <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                            <select name="roleId" class="form-control input-sm" style="width:110px;display:inline;"
                                    onchange="this.form.submit()">
                                <% Long currentRole = roleMap != null ? roleMap.get(u.getUserId()) : null;
                                   int cur = currentRole != null ? currentRole.intValue() : 0;
                                   for (int i = 2; i <= 4; i++) { %>
                                <option value="<%= i %>" <%= i == cur ? "selected" : "" %>><%= roleNames[i] %></option>
                                <% } %>
                            </select>
                        </form>
                    </td>
                    <td>
                        <% if (u.getStatus() == 1) { %>
                        <span class="card-tag tag-free">正常</span>
                        <% } else { %>
                        <span class="card-tag tag-views">已禁用</span>
                        <% } %>
                    </td>
                    <td><%= u.getCreatedAt() %></td>
                    <td>
                        <% if (u.getStatus() == 1) { %>
                        <a href="admin?action=disableUser&userId=<%= u.getUserId() %>&status=0"
                           class="btn btn-default btn-xs" style="color:#c62828;"
                           onclick="return confirm('确定禁用该用户？')">禁用</a>
                        <% } else { %>
                        <a href="admin?action=disableUser&userId=<%= u.getUserId() %>&status=1"
                           class="btn btn-default btn-xs" style="color:#2e7d32;"
                           onclick="return confirm('确定启用该用户？')">启用</a>
                        <% } %>
                    </td>
                </tr>
                <% }} %>
            </tbody>
        </table>
    </div>

    <!-- 分页 -->
    <% if (totalPages > 1) { %>
    <nav class="text-center">
        <ul class="pagination">
            <% if (currentPage > 1) { %>
            <li><a href="admin?action=users&page=<%= currentPage - 1 %>">&laquo;</a></li>
            <% } %>
            <% for (int i = 1; i <= totalPages; i++) { %>
            <li <%= i == currentPage ? "class=\"active\"" : "" %>>
                <a href="admin?action=users&page=<%= i %>"><%= i %></a>
            </li>
            <% } %>
            <% if (currentPage < totalPages) { %>
            <li><a href="admin?action=users&page=<%= currentPage + 1 %>">&raquo;</a></li>
            <% } %>
        </ul>
    </nav>
    <% } %>

    <a href="admin?action=dashboard" class="btn btn-default">返回仪表盘</a>
</div>

<%@ include file="../common/footer.jsp" %>
