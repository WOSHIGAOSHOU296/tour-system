<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tour.service.ScenicService" %>
<%@ page import="com.tour.model.Scenic" %>
<%@ page import="java.util.List" %>
<%
    ScenicService scenicService = new ScenicService();
    List<Scenic> hotList = scenicService.getHotTop(10);
    StringBuilder chartNames = new StringBuilder("[");
    StringBuilder chartViews = new StringBuilder("[");
    for (int i = 0; i < hotList.size(); i++) {
        Scenic s = hotList.get(i);
        if (i > 0) { chartNames.append(","); chartViews.append(","); }
        chartNames.append("'").append(s.getScenicName()).append("'");
        chartViews.append(s.getViewCount());
    }
    chartNames.append("]");
    chartViews.append("]");
%>
<%@ include file="common/header.jsp" %>

<!-- Hero -->
<div class="hero-section">
    <div class="container">
        <h1>探索世界，从这里开始</h1>
        <p>个性化旅游定制平台，为您量身打造完美旅程</p>
    </div>
</div>

<div class="container">
    <!-- 热门景点 -->
    <div class="section-title"><h2>&#128293; 热门景点</h2></div>
    <p style="padding-left:23px;color:#999;margin-bottom:24px;">浏览量最高的推荐目的地</p>

    <div class="row">
        <% for (int i = 0; i < Math.min(6, hotList.size()); i++) {
            Scenic s = hotList.get(i); %>
        <div class="col-md-4 col-sm-6">
            <div class="thumbnail scenic-card">
                <div class="scenic-img-placeholder"
                     style="background: hsl(<%= (i * 60) % 360 %>, 60%, 65%);">
                    <span style="font-size:48px;color:#fff;"><%= s.getScenicName().substring(0, Math.min(3, s.getScenicName().length())) %></span>
                </div>
                <div class="caption">
                    <h4><%= s.getScenicName() %></h4>
                    <p class="text-muted small">
                        &#128205; <%= s.getCityName() %> &middot; <%= s.getScenicType() %>
                    </p>
                    <p>
                        <span class="card-tag tag-score">&#11088; <%= s.getAverageScore() %>分</span>
                        <span class="card-tag tag-views">&#128065; <%= s.getViewCount() %>次</span>
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
    </div>

    <!-- ECharts 可视化 -->
    <div class="section-title" style="margin-top:40px;"><h2>&#128202; 景点热度排行</h2></div>
    <p style="padding-left:23px;color:#999;margin-bottom:24px;">基于浏览量数据的可视化展示</p>
    <div id="chart-hot" style="width:100%;height:400px;background:#fff;border-radius:16px;padding:20px;box-shadow:0 2px 12px rgba(0,0,0,0.06);margin-bottom:20px;"></div>
</div>

<script>
var myChart = echarts.init(document.getElementById('chart-hot'));
myChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: <%= chartNames.toString() %>, axisLabel: { rotate: 30, color: '#8d6e63' } },
    yAxis: { type: 'value', name: '浏览量', nameTextStyle: { color: '#8d6e63' } },
    series: [{
        data: <%= chartViews.toString() %>, type: 'bar',
        itemStyle: { color: new echarts.graphic.LinearGradient(0,0,0,1,[
            { offset: 0, color: '#ff8f00' }, { offset: 1, color: '#e65100' }
        ])},
        barWidth: '50%'
    }]
});
window.addEventListener('resize', function() { myChart.resize(); });
</script>

<%@ include file="common/footer.jsp" %>
