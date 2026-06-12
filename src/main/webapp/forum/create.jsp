<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="page-header" style="border:none;">
        <h2 style="color:#bf360c;font-weight:bold;">&#9997; 发布新帖</h2>
    </div>

    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel form-panel">
                <div class="panel-heading">分享你的旅行故事</div>
                <div class="panel-body" style="padding:30px;">
                    <form action="forum" method="post">
                        <input type="hidden" name="action" value="doCreate">
                        <div class="form-group">
                            <label>分类</label>
                            <select name="category" class="form-control" required>
                                <option value="">请选择分类</option>
                                <option value="攻略">攻略</option>
                                <option value="结伴">结伴</option>
                                <option value="问答">问答</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>标题</label>
                            <input type="text" name="title" class="form-control"
                                   placeholder="请输入帖子标题" maxlength="50" required>
                        </div>
                        <div class="form-group">
                            <label>内容</label>
                            <textarea name="content" class="form-control" rows="8"
                                      placeholder="请输入帖子内容，最多500字" maxlength="500" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">发 布</button>
                        <a href="forum?action=list" class="btn btn-default">取消</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
