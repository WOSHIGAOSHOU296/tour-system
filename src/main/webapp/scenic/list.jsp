<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Scenic" %>
<%@ page import="com.tour.model.City" %>
<%
    List<Scenic> scenics = (List<Scenic>) request.getAttribute("scenics");
    List<City> cities = (List<City>) request.getAttribute("cities");
    int total = (Integer) request.getAttribute("total");
    int currentPage = (Integer) request.getAttribute("page");
    int totalPages = (Integer) request.getAttribute("totalPages");
    Long currentCityId = (Long) request.getAttribute("currentCityId");
    String currentKeyword = (String) request.getAttribute("currentKeyword");
    if (currentKeyword == null) currentKeyword = "";
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#127963; 景点浏览</h2>
    </div>

    <!-- 搜索和筛选 -->
    <div class="well">
        <form action="${pageContext.request.contextPath}/scenic" method="get" class="form-inline">
            <input type="hidden" name="action" value="list">
            <div class="form-group" style="margin-right:10px;">
                <select name="cityId" class="form-control">
                    <option value="">全部城市</option>
                    <% if (cities != null) { for (City c : cities) { %>
                    <option value="<%= c.getCityId() %>"
                            <%= (currentCityId != null && currentCityId.equals(c.getCityId())) ? "selected" : "" %>>
                        <%= c.getCityName() %>
                    </option>
                    <% }} %>
                </select>
            </div>
            <div class="form-group" style="margin-right:10px;">
                <input type="text" name="keyword" class="form-control" placeholder="搜索景点..."
                       value="<%= currentKeyword %>" style="width:250px;">
            </div>
            <button type="submit" class="btn btn-primary">&#128269; 搜索</button>
        </form>
    </div>

    <p class="text-muted" style="margin-top:16px;">共找到 <strong style="color:#e65100;"><%= total %></strong> 个景点</p>

    <div class="row">
        <% if (scenics != null && !scenics.isEmpty()) {
            for (Scenic s : scenics) { %>
        <div class="col-md-4 col-sm-6">
            <div class="thumbnail scenic-card">
                <div class="scenic-img-placeholder" style="overflow:hidden;">
                    <img src="${pageContext.request.contextPath}/<%= s.getImgUrl() %>" class="scenic-img"
                         onerror="this.style.display='none';this.parentElement.style.background='hsl(<%= (s.getScenicId() * 47) % 360 %>, 55%, 60%)';this.parentElement.innerHTML='<span style=font-size:40px;color:#fff><%= s.getScenicName().substring(0, Math.min(3, s.getScenicName().length())) %></span>'"
                         alt="<%= s.getScenicName() %>">
                </div>
                <div class="caption">
                    <h4>
                        <a href="${pageContext.request.contextPath}/scenic?action=detail&id=<%= s.getScenicId() %>">
                            <%= s.getScenicName() %>
                        </a>
                    </h4>
                    <p class="text-muted small">
                        &#128205; <%= s.getCityName() %> &middot; &#127991; <%= s.getScenicType() %>
                    </p>
                    <p class="small" style="line-height:1.6;">
                        <%= s.getIntroduce() != null && s.getIntroduce().length() > 60 ?
                            s.getIntroduce().substring(0, 60) + "..." : s.getIntroduce() %>
                    </p>
                    <p>
                        <span class="card-tag tag-score">&#11088; <%= s.getAverageScore() %>分</span>
                        <span class="card-tag tag-views">&#128065; <%= s.getViewCount() %></span>
                        <% if (s.getTicketPrice() != null && s.getTicketPrice().doubleValue() > 0) { %>
                        <span class="card-tag tag-price">&yen;<%= s.getTicketPrice() %></span>
                        <% } else { %>
                        <span class="card-tag tag-free">免费</span>
                        <% } %>
                    </p>
                    <a href="${pageContext.request.contextPath}/scenic?action=detail&id=<%= s.getScenicId() %>"
                       class="btn btn-primary btn-sm btn-block">查看详情</a>
                </div>
            </div>
        </div>
        <% } %>
        <% } else { %>
        <div class="col-md-12">
            <div class="alert alert-info text-center" style="padding:40px;border-radius:16px;">暂无景点数据</div>
        </div>
        <% } %>
    </div>

    <!-- 分页 -->
    <% if (totalPages > 1) { %>
    <nav class="text-center">
        <ul class="pagination">
            <% if (currentPage > 1) { %>
            <li><a href="?action=list&cityId=<%= currentCityId != null ? currentCityId : "" %>&keyword=<%= currentKeyword %>&page=<%= currentPage - 1 %>">&laquo;</a></li>
            <% } %>
            <% for (int i = 1; i <= totalPages; i++) { %>
            <li <%= i == currentPage ? "class=\"active\"" : "" %>>
                <a href="?action=list&cityId=<%= currentCityId != null ? currentCityId : "" %>&keyword=<%= currentKeyword %>&page=<%= i %>"><%= i %></a>
            </li>
            <% } %>
            <% if (currentPage < totalPages) { %>
            <li><a href="?action=list&cityId=<%= currentCityId != null ? currentCityId : "" %>&keyword=<%= currentKeyword %>&page=<%= currentPage + 1 %>">&raquo;</a></li>
            <% } %>
        </ul>
    </nav>
    <% } %>
</div>

<%@ include file="../common/footer.jsp" %>
