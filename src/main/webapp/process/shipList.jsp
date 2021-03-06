<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>
<form class="form-inline" action="<%=request.getContextPath()%>/process/shipList" method="GET">
<h3 class="sub-header">出库流程列表
	<span>
		<select class="selectpicker" data-live-search="true" data-width="fit" id="hospitalId" name="hospitalId">
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
		<a id="generateBtn" type="button" class="btn btn-success" style="" onClick="printList(${hospitalId})">生成出库清单</a>
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
			<c:forEach var="prs" items="${shipList}">
				<tr>
					<td><c:out value="${prs.uuid}" /></td>
					<td><c:out value="${prs.hospital_name}" /></td>
					<td><c:out value="${prs.outer_id}" /></td>
					<td><c:out value="${prs.patient_name}" /></td>
					<td><c:out value="${prs.packet_num}" /></td>
					<td><c:out value="${prs.price}" /></td>
					<td><c:out value="${prs.create_time}" /></td>
					<td width="100">
						<div class="btn-group" role="group" aria-label="...">
							<a type="button" class="btn btn-default" href="<%=request.getContextPath()%>/prescription/modify?prsId=${prs.id}&from=SHIP">处方修改</a>
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
    var printList = function(hospitalId){
    	$("#assureMsg").html("确认生成出库清单?");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/printShipListXls?hospitalId="+hospitalId);
        $("#assureDlg").modal("show");
    };
</script>
<script>
if ($("#hospitalId").val() == 0) {
    $("#generateBtn").hide();
} else {
    $("#generateBtn").show();
}

$("#hospitalId").change(function(){
	if ($("#hospitalId").val() == 0) {
	    $("#generateBtn").hide();
	} else {
	    $("#generateBtn").show();
	}
}); 
</script>
<script>
$(".selectpicker").change(function(){
    $(".form-inline").submit();
}); 
</script>
<%@ include file="../foot.jsp"%>