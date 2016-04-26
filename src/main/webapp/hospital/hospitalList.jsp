<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">医院列表<a class="btn btn-primary" style="float:right;margin-top:-5px" href="<%=request.getContextPath()%>/hospital/add">添加医院</a></h3>
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
				<th>医院名称</th>
				<th>医院简介</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="hospital" items="${hospitalList}">
				<tr>
					<td><c:out value="${hospital.id}" /></td>
					<td><c:out value="${hospital.name}" /></td>
					<td><c:out value="${hospital.description}" /></td>
					<td width="100">
						<div class="btn-group" role="group" aria-label="...">
							<a type="button" class="btn btn-danger" onClick="deleteHospital(${hospital.id}, '${hospital.name}');">删除医院</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<c:set var="pageUrl" value="hospital/list" />
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
    var deleteHospital = function(id, hospital_name){
    	$("#assureMsg").html("确认删除医院<strong>"+hospital_name+"</strong>？");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/hospital/delete?hospitalId="+id+"&pageNum=${page.pageNum}");
        $("#assureDlg").modal("show");
    };
</script>
<%@ include file="../foot.jsp"%>