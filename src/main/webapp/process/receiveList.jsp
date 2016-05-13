<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>
<form class="form-inline" action="<%=request.getContextPath()%>/process/receiveList" method="GET">
<h3 class="sub-header">接方流程列表
	<span>
		<select class="selectpicker" data-live-search="true" data-width="fit" id="hospitalId" name="hospitalId">
			<option value=0>全部医院</option>
			
			<c:forEach var='hosp' items="${hospitalList}">
				<c:if test="${hospitalId == hosp.id}">
				<option value="${hosp.id}" selected>${hosp.name}</option>
				</c:if>
				<c:if test="${hospitalId != hosp.id}">
				<option value="${hosp.id}">${hosp.name}</option>
				</c:if>
			</c:forEach>
		</select>
		<button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
		<a type="button" class="btn btn-success" style="" onClick="printPrs(${hospitalId})">打印处方标签</a>
		<a class="btn btn-primary" href="<%=request.getContextPath()%>/prescription/add">添加处方</a>
	</span>
	</h3>
</form>
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
				<th>编号</th>
				<th>医院</th>
				<th>医院编号</th>
				<th>姓名</th>
				<th>贴数</th>
				<th>金额</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="prs" items="${receiveList}">
				<tr>
					<td><c:out value="${prs.uuid}" /></td>
					<td><c:out value="${prs.hospital_name}" /></td>
					<td><c:out value="${prs.outer_id}" /></td>
					<td><c:out value="${prs.patient_name}" /></td>
					<td><c:out value="${prs.packet_num}" /></td>
					<td><c:out value="${prs.price}" /></td>
					<td><c:out value="${prs.create_time}" /></td>
					<td width="200">
						<div class="btn-group" role="group" aria-label="...">
							<a type="button" class="btn btn-default" href="<%=request.getContextPath()%>/prescription/modify?prsId=${prs.id}&from=RECEIVE">处方修改</a>
							<% if (request.getAttribute("hospitalId") != null){ %>
							<a type="button" class="btn btn-danger" onClick="deletePrs(${prs.id},${prs.process},'${prs.hospital_name}','${prs.outer_id}',${hospitalId});">删除处方</a>
							<% } else { %>
							<a type="button" class="btn btn-danger" onClick="deletePrs(${prs.id},${prs.process},'${prs.hospital_name}','${prs.outer_id}',0);">删除处方</a>
							<% } %>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
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
    var deletePrs = function(id, process, hospital_name, outer_id, selectedHospital){
    	$("#assureMsg").html("确认删除处方 <strong>"+hospital_name+":"+outer_id+"</strong>？");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/delete?prsId="+id+"&hospitalId="+selectedHospital);
        $("#assureDlg").modal("show");
    };
    
    var printPrs = function(hospitalId){
    	$("#assureMsg").html("确认打印处方标签?");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/printReceiveList?hospitalId="+hospitalId);
        $("#assureDlg").modal("show");
    };
</script>
<script>
$(".selectpicker").change(function(){
    $(".form-inline").submit();
}); 
</script>
<%@ include file="../foot.jsp"%>