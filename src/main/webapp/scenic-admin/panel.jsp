<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Scenic" %>
<%
    List<Scenic> scenics = (List<Scenic>) request.getAttribute("scenics");
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#128736; 景区管理中心</h2>
        <p class="text-muted">选择要管理的景区，进行公告发布和留言管理</p>
    </div>

    <div class="row">
        <% if (scenics != null && !scenics.isEmpty()) {
            for (Scenic s : scenics) { %>
        <div class="col-md-4 col-sm-6">
            <div class="thumbnail scenic-card">
                <div class="scenic-img-placeholder"
                     style="background: hsl(<%= (s.getScenicId() * 47) % 360 %>, 55%, 60%);height:160px;display:flex;align-items:center;justify-content:center;">
                    <span style="font-size:40px;color:#fff;"><%= s.getScenicName().substring(0, Math.min(3, s.getScenicName().length())) %></span>
                </div>
                <div class="caption">
                    <h4><%= s.getScenicName() %></h4>
                    <p class="text-muted small">&#128205; <%= s.getCityName() %> | &#128065; <%= s.getViewCount() %>次浏览</p>
                    <a href="scenicAdmin?action=manage&scenicId=<%= s.getScenicId() %>"
                       class="btn btn-primary btn-block btn-sm">管理此景区</a>
                </div>
            </div>
        </div>
        <% }} %>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
