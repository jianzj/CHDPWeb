<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>

<form class="form-inline" action="<%=request.getContextPath()%>/prescription/currentList" method="GET">
<h3 class="sub-header">
	当前处方列表
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
		<select class="selectpicker" data-live-search="true" data-with="fit" id="process" name="process">
			<option value=0>全部流程</option>
			<c:if test="${process == 1}">
			<option value=1 selected>接方</option>
			</c:if>
			<c:if test="${process != 1}">
			<option value=1>接方</option>
			</c:if>
			<c:if test="${process == 2}">
			<option value=2 selected>审方</option>
			</c:if>
			<c:if test="${process != 2}">
			<option value=2>审方</option>
			</c:if>
			<c:if test="${process == 3}">
			<option value=3 selected>调配</option>
			</c:if>
			<c:if test="${process != 3}">
			<option value=3>调配</option>
			</c:if>
			<c:if test="${process == 4}">
			<option value=4 selected>调配审核</option>
			</c:if>
			<c:if test="${process != 4}">
			<option value=4>调配审核</option>
			</c:if>
			<c:if test="${process == 5}">
			<option value=5 selected>浸泡</option>
			</c:if>
			<c:if test="${process != 5}">
			<option value=5>浸泡</option>
			</c:if>
			<c:if test="${process == 6}">
			<option value=6 selected>煎煮</option>
			</c:if>
			<c:if test="${process != 6}">
			<option value=6>煎煮</option>
			</c:if>
			<c:if test="${process == 7}">
			<option value=7 selected>灌装</option>
			</c:if>
			<c:if test="${process != 7}">
			<option value=7>灌装</option>
			</c:if>
			<c:if test="${process == 8}">
			<option value=8 selected>清场</option>
			</c:if>
			<c:if test="${process != 8}">
			<option value=8>清场</option>
			</c:if>
			<c:if test="${process == 9}">
			<option value=9 selected>包装</option>
			</c:if>
			<c:if test="${process != 9}">
			<option value=9>包装</option>
			</c:if>
			<c:if test="${process == 10}">
			<option value=10 selected>出库</option>
			</c:if>
			<c:if test="${process != 10}">
			<option value=10>出库</option>
			</c:if>			
		</select>
		<button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
		<a type="button" class="btn btn-danger" style="" onClick="deletePrsSelected(${hospitalId}, ${process})">删除处方</a>
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
			    <th><input type="checkbox" value=0 class="check" id="checkAll"></th>
				<th>编号</th>
				<th>医院</th>
				<th>医院编号</th>
				<th>姓名</th>
				<th>贴数</th>
				<th>当前状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="prs" items="${currentPrsList}">
				<tr>
				    <td><input type="checkbox" value="${prs.id}" class="check"/></td>
					<td><c:out value="${prs.uuid}" /></td>
					<td><c:out value="${prs.hospital_name}" /></td>
					<td><c:out value="${prs.outer_id}" /></td>
					<td><c:out value="${prs.patient_name}" /></td>
					<td><c:out value="${prs.packet_num}" /></td>
					<td><c:out value="${prs.phase_name}" /></td>
					<td width="320">
						<div class="btn-group" role="group" aria-label="...">
							<% if (request.getParameter("pageNum") == null){ %>
							<% 		request.setAttribute("page_num", 1); %>
							<% }else{ %>
							<% 		request.setAttribute("page_num", request.getParameter("pageNum")); %>
							<% } %>
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/process/showAllProcs?prsId=${prs.id}&from=CURRENT">状态</a>
							<a type="button" class="btn btn-default" href="<%=request.getContextPath()%>/prescription/modify?prsId=${prs.id}&from=CURRENT">修改</a>
							<a type="button" class="btn btn-primary" onClick="printSinglePrs(${prs.id},${hospitalId},${process},${page_num},'${prs.hospital_name}','${prs.outer_id}')">打印标签</a>
							<a type="button" class="btn btn-success" onClick="printSinglePackage(${prs.id},${hospitalId},${process},${page_num},'${prs.hospital_name}','${prs.outer_id}')">打印包装标签</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<% request.setAttribute("hospitalId", (Integer)request.getAttribute("hospitalId")); %>
	<% request.setAttribute("process", (Integer)request.getAttribute("process")); %>
	<c:set var="pageUrl" value="prescription/currentList" />
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
	var printSinglePackage = function(id, selectedHospital, process, pageNum, hospital_name, outer_id){
		$("#assureMsg").html("打印处方包装标签 <strong>"+hospital_name+":"+outer_id+"</strong> ？");
	    $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/printSingleLabel?printType=PACKAGE&from=CURRENT&prsId="+id+"&process="+process+"&hospitalId="+selectedHospital+"&pageNum="+pageNum);
	    $("#assureDlg").modal("show");
	};

	var printSinglePrs = function(id, selectedHospital, process, pageNum, hospital_name, outer_id){
		$("#assureMsg").html("打印处方标签 <strong>"+hospital_name+":"+outer_id+"</strong> ？");
	    $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/printSingleLabel?printType=PRS&from=CURRENT&prsId="+id+"&process="+process+"&hospitalId="+selectedHospital+"&pageNum="+pageNum);
	    $("#assureDlg").modal("show");
	};
</script>
<script>
$(".selectpicker").change(function(){
    $(".form-inline").submit();
}); 
</script>
<script>
$("#checkAll").click(function () {
    $(".check").prop('checked', $(this).prop('checked'));
});
</script>
<script>
var deletePrsSelected = function(hospitalId, process){
	var prsList = [];
	$.each($("input[class='check']:checked"), function(){
		if ($(this).val() > 0){
			prsList.push($(this).val());
		}
    });
	if (prsList.length == 0){
		alert("您未选择任何需要删除的处方！");
	}else{
		prsListStr = prsList.join("/");
		$("#assureMsg").html("确认删除处方?");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/deletePrsSelected?hospitalId="+hospitalId+"&prsList="+prsListStr+"&process="+process+"&pageFrom=currentList");
        $("#assureDlg").modal("show");
	}
}
</script>
<%@ include file="../foot.jsp"%>