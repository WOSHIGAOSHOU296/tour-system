<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tour.model.Scenic" %>
<%@ page import="com.tour.model.Food" %>
<%@ page import="com.tour.model.Hotel" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Message" %>
<%
    Scenic scenic = (Scenic) request.getAttribute("scenic");
    List<Food> foods = (List<Food>) request.getAttribute("foods");
    List<Hotel> hotels = (List<Hotel>) request.getAttribute("hotels");
    List<Message> messages = (List<Message>) request.getAttribute("messages");
    if (scenic == null) {
        response.sendRedirect(request.getContextPath() + "/scenic?action=list");
        return;
    }
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <ol class="breadcrumb">
        <li><a href="${pageContext.request.contextPath}/index.jsp">首页</a></li>
        <li><a href="${pageContext.request.contextPath}/scenic?action=list">景点列表</a></li>
        <li class="active"><%= scenic.getScenicName() %></li>
    </ol>

    <div class="row">
        <div class="col-md-8">
            <!-- 景点图片 -->
            <div class="detail-img-placeholder"
                 style="background:linear-gradient(135deg,#ff8a65,#ff7043);height:350px;display:flex;align-items:center;justify-content:center;">
                <span style="font-size:72px;color:rgba(255,255,255,0.6);">
                    <%= scenic.getScenicName().substring(0, Math.min(4, scenic.getScenicName().length())) %>
                </span>
            </div>

            <!-- 景点简介 -->
            <div class="section-panel panel panel-default">
                <div class="panel-heading" style="background:#fff3e0;color:#bf360c;">景点简介</div>
                <div class="panel-body" style="line-height:1.8;">
                    <p><%= scenic.getIntroduce() %></p>
                </div>
            </div>

            <!-- 周边美食 -->
            <div class="section-panel panel" style="border-top:4px solid #ff8f00;">
                <div class="panel-heading" style="background:#fff8e1;color:#e65100;">&#127860; 周边美食</div>
                <div class="panel-body">
                    <% if (foods != null && !foods.isEmpty()) { %>
                    <div class="list-group" style="border:none;">
                        <% for (Food f : foods) { %>
                        <div class="list-group-item" style="border:none;border-radius:12px;margin-bottom:8px;background:#fdf6f0;">
                            <h5 style="font-weight:bold;color:#4a3728;"><%= f.getFoodName() %></h5>
                            <p class="text-muted small">&#128205; <%= f.getAddress() %></p>
                            <p><%= f.getIntroduce() %></p>
                        </div>
                        <% } %>
                    </div>
                    <% } else { %>
                    <p class="text-muted">暂无美食信息</p>
                    <% } %>
                </div>
            </div>

            <!-- 周边酒店 -->
            <div class="section-panel panel" style="border-top:4px solid #e65100;">
                <div class="panel-heading" style="background:#fbe9e7;color:#bf360c;">&#127968; 周边住宿</div>
                <div class="panel-body">
                    <% if (hotels != null && !hotels.isEmpty()) { %>
                    <div class="list-group" style="border:none;">
                        <% for (Hotel h : hotels) { %>
                        <div class="list-group-item" style="border:none;border-radius:12px;margin-bottom:8px;background:#fdf6f0;">
                            <h5 style="font-weight:bold;color:#4a3728;"><%= h.getHotelName() %></h5>
                            <p class="text-muted small">&#128205; <%= h.getAddress() %></p>
                            <span class="card-tag tag-price"><%= h.getPriceRange() %></span>
                        </div>
                        <% } %>
                    </div>
                    <% } else { %>
                    <p class="text-muted">暂无酒店信息</p>
                    <% } %>
                </div>
            </div>

            <!-- 景点留言板 -->
            <div class="section-panel panel" style="border-top:4px solid #ff8f00;margin-top:20px;">
                <div class="panel-heading" style="background:#fff8e1;color:#e65100;">
                    &#128221; 景点留言板 (<%= messages != null ? messages.size() : 0 %>)
                </div>
                <div class="panel-body">
                    <% if (messages != null && !messages.isEmpty()) {
                        for (Message m : messages) { %>
                    <div class="comment-box">
                        <div>
                            <strong style="color:#e65100;"><%= m.getUsername() %></strong>
                            <small class="text-muted"> &middot; <%= m.getCreatedAt() %></small>
                            <% if (roleId != null && roleId == 4) { %>
                            <a href="scenic?action=deleteMessage&messageId=<%= m.getMessageId() %>&scenicId=<%= scenic.getScenicId() %>"
                               class="text-danger pull-right" onclick="return confirm('确定删除该留言？')"
                               style="font-size:12px;">删除</a>
                            <% } %>
                        </div>
                        <p style="margin-top:6px;"><%= m.getContent() %></p>
                    </div>
                    <% }} else { %>
                    <p class="text-muted">暂无留言，快来发表第一条留言吧</p>
                    <% } %>
                </div>
            </div>

            <!-- 发表留言表单 -->
            <% if (sessionUser != null) { %>
            <div class="section-panel panel panel-default" style="margin-top:10px;">
                <div class="panel-heading" style="background:#fdf6f0;">发表留言</div>
                <div class="panel-body">
                    <form action="scenic" method="post">
                        <input type="hidden" name="action" value="doMessage">
                        <input type="hidden" name="scenicId" value="<%= scenic.getScenicId() %>">
                        <div class="form-group">
                            <textarea name="content" class="form-control" rows="3"
                                      placeholder="写下你对这个景点的评价或留言..." required
                                      style="border-radius:12px;"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">发表留言</button>
                    </form>
                </div>
            </div>
            <% } else { %>
            <div class="alert alert-info" style="margin-top:10px;padding:16px;border-radius:12px;">
                请先 <a href="${pageContext.request.contextPath}/login.jsp" style="color:#e65100;">登录</a> 后发表留言
            </div>
            <% } %>
        </div>

        <!-- 侧边栏 -->
        <div class="col-md-4">
            <div class="info-panel panel">
                <div class="panel-heading"><%= scenic.getScenicName() %></div>
                <div class="panel-body" style="padding:0;">
                    <table class="table table-striped" style="margin:0;">
                        <tr><th>所属城市</th><td><%= scenic.getCityName() %></td></tr>
                        <tr><th>景点类型</th><td><%= scenic.getScenicType() %></td></tr>
                        <tr><th>详细地址</th><td><%= scenic.getAddress() %></td></tr>
                        <tr><th>门票价格</th><td>
                            <% if (scenic.getTicketPrice() != null && scenic.getTicketPrice().doubleValue() > 0) { %>
                            <span class="text-danger"><strong>&yen;<%= scenic.getTicketPrice() %></strong></span>
                            <% } else { %>免费<% } %>
                        </td></tr>
                        <tr><th>开放时间</th><td><%= scenic.getOpenTime() %></td></tr>
                        <tr><th>综合评分</th><td>
                            <span style="color:#ff8f00;">
                                <% for (int i = 0; i < Math.round(scenic.getAverageScore().doubleValue()); i++) { %>&#9733;<% } %>
                            </span>
                            <%= scenic.getAverageScore() %>分
                        </td></tr>
                        <tr><th>浏览量</th><td><strong style="color:#e65100;"><%= scenic.getViewCount() %></strong>次</td></tr>
                    </table>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/collection?action=toggle&type=1&targetId=<%= scenic.getScenicId() %>&redirect=scenic?action=detail&id=<%= scenic.getScenicId() %>"
               class="btn btn-default btn-block" style="margin-top:12px;">&#11088; 收藏</a>
            <a href="${pageContext.request.contextPath}/scenic?action=list"
               class="btn btn-default btn-block" style="margin-top:6px;">返回列表</a>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
