<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Scenic" %>
<%
    List<Scenic> scenics = (List<Scenic>) request.getAttribute("scenics");
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#9997; 发布景点攻略</h2>
    </div>

    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel form-panel">
                <div class="panel-heading">分享你的旅行经验</div>
                <div class="panel-body" style="padding:30px;">
                    <form action="strategy" method="post">
                        <input type="hidden" name="action" value="doCreate">
                        <div class="form-group">
                            <label>关联景点</label>
                            <select name="scenicId" class="form-control" required>
                                <option value="">请选择景点</option>
                                <% if (scenics != null) {
                                    for (Scenic s : scenics) { %>
                                <option value="<%= s.getScenicId() %>"><%= s.getScenicName() %> - <%= s.getCityName() %></option>
                                <% }} %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>攻略标题</label>
                            <input type="text" name="title" class="form-control"
                                   placeholder="请输入攻略标题" maxlength="50" required>
                        </div>
                        <div class="form-group">
                            <label>攻略内容</label>
                            <textarea name="content" class="form-control" rows="10"
                                      placeholder="分享你的旅行经验、避坑指南、交通建议..." maxlength="500" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">发 布</button>
                        <a href="strategy?action=list" class="btn btn-default">取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
