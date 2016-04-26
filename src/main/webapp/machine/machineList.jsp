<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">机器列表<a class="btn btn-primary" style="float:right;margin-top:-5px" href="<%=request.getContextPath()%>/machine/add">添加机器</a></h3>
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
				<th>机器型号</th>
				<th>机器名称</th>
				<th>机器描述</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="machine" items="${machineList}">
				<tr>
					<td><c:out value="${machine.id}" /></td>
					<td><c:out value="${machine.type}" /></td>
					<td><c:out value="${machine.name}" /></td>
					<td><c:out value="${machine.description}" /></td>
					<td width="300">
						<div class="btn-group" role="group" aria-label="...">
							<a type="button" class="btn btn-default" href="<%=request.getContextPath()%>/user/modify?userId=">用户修改</a> <a type="button" class="btn btn-default"
								onClick="resetUserPassword');">重置密码</a> <a type="button" class="btn btn-danger"
								onClick="deleteUser(');">删除用户</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<c:set var="pageUrl" value="user/list" />
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
    var deleteUser = function(id,usercode){
    	$("#assureMsg").html("确认删除用户<strong>"+usercode+"</strong>？");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/user/delete?userId="+id+"&pageNum=${page.pageNum}");
        $("#assureDlg").modal("show");
    };
    var resetUserPassword = function(id,usercode){
    	$("#assureMsg").html("确认重置用户<strong>"+usercode+"</strong>的密码？");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/user/resetPassword?userId="+id+"&pageNum=${page.pageNum}");
        $("#assureDlg").modal("show");
    };
</script>
<%@ include file="../foot.jsp"%>