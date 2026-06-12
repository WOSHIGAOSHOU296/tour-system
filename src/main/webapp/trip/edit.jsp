<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tour.model.Scenic" %>
<%@ page import="com.tour.model.Trip" %>
<%
    Trip trip = (Trip) request.getAttribute("trip");
    List<Scenic> scenics = (List<Scenic>) request.getAttribute("scenics");
    if (trip == null) {
        response.sendRedirect(request.getContextPath() + "/trip?action=list");
        return;
    }
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#9998; 编辑行程</h2>
    </div>

    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel form-panel">
                <div class="panel-heading"><%= trip.getTripName() %></div>
                <div class="panel-body" style="padding:30px;">
                    <form action="trip" method="post" onsubmit="return checkForm()">
                        <input type="hidden" name="action" value="doEdit">
                        <input type="hidden" name="tripId" value="<%= trip.getTripId() %>">
                        <div class="form-group">
                            <label>行程名称</label>
                            <input type="text" name="tripName" id="tripName" class="form-control"
                                   value="<%= trip.getTripName() %>" maxlength="50" required>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>出发日期</label>
                                    <input type="date" name="startDate" id="startDate" class="form-control"
                                           value="<%= trip.getStartDate() != null ? sdf.format(trip.getStartDate()) : "" %>" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>结束日期</label>
                                    <input type="date" name="endDate" id="endDate" class="form-control"
                                           value="<%= trip.getEndDate() != null ? sdf.format(trip.getEndDate()) : "" %>" required>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>行程安排</label>
                            <p class="text-muted small" style="margin:0 0 8px;">手动填写景点、住宿、美食安排</p>
                            <textarea name="tripContent" class="form-control" rows="12"
                                      maxlength="1000"><%= trip.getTripContent() != null ? trip.getTripContent() : "" %></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">保 存</button>
                        <a href="trip?action=list" class="btn btn-default">取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function checkForm() {
    var start = new Date(document.getElementById('startDate').value);
    var end = new Date(document.getElementById('endDate').value);
    if (end < start) {
        alert('结束日期不能早于出发日期');
        return false;
    }
    return true;
}
</script>

<%@ include file="../common/footer.jsp" %>
