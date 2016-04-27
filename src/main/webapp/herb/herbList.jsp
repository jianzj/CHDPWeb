<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">中药列表<a class="btn btn-primary" style="float:right;margin-top:-5px" href="<%=request.getContextPath()%>/herb/add">添加中药</a></h3>
<div>
	<c:if test="${not empty errorMsg}">
		<div class="alert alert-danger" role="alert">${errorMsg}</div>
	</c:if>
	<c:if test="${not empty successMsg}">
		<div class="alert alert-success" role="alert">${successMsg}</div>
	</c:if>
</div>
<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>ID</th>
				<th>中药类型</th>
				<th>中药名称</th>
				<th>中药描述</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="herb" items="${herbList}">
				<tr>
					<td><c:out value="${herb.id}" /></td>
					<c:if test="${herb.type == 1}">
						<td><c:out value="先煎" /></td>
					</c:if>
					<c:if test="${herb.type == 2}">
						<td><c:out value="后下" /></td>
					</c:if>
					<c:if test="${herb.type == 3}">
						<td><c:out value="包煎" /></td>
					</c:if>
					<td><c:out value="${herb.name}" /></td>
					<td><c:out value="${herb.description}" /></td>
					<td width="100">
						<div class="btn-group" role="group" aria-label="...">
							<a type="button" class="btn btn-danger" onClick="deleteHerb(${herb.id}, '${herb.name}');">删除中药</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<c:set var="pageUrl" value="herb/list" />
	<%@ include file="../common/nav.jsp"%>
</div>
<div class="modal fade" id="assureDlg" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-body" id="assureMsg"></div>
			<div class="modal-footer">
				<a type="button" class="btn btn-success" id="assureBtn">确认</a> <a type="button" class="btn btn-default"
					data-dismiss="modal">取消</a>
			</div>
		</div>
	</div>
</div>
<script>
    var deleteHerb = function(id, herb_name){
    	$("#assureMsg").html("确认删除中药<strong>"+herb_name+"</strong>？");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/herb/delete?herbId="+id+"&pageNum=${page.pageNum}");
        $("#assureDlg").modal("show");
    };
</script>
<%@ include file="../foot.jsp"%>