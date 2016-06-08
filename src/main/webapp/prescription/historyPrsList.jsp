<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>

<form class="form-inline" action="<%=request.getContextPath()%>/prescription/historyList" method="GET">
<h3 class="sub-header">
	历史处方列表
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
		<span class="input-group input-append date col-xs-2" id="hospital-datePicker-start">
			<input type="text" class="form-control" name="startTime" value="${startTime}"/>
            <span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
         </span>
         <span class="input-group input-append date col-xs-2" id="hospital-datePicker-end">
                <input type="text" class="form-control" name="endTime" value="${endTime}"/>
         <span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
         </span>
		<button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
		<a type="button" class="btn btn-danger" style="" onClick="deletePrsSelected(${hospitalId}, '${startTime}', '${endTime}')">删除处方</a>
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
				<th>结束时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="prs" items="${historyPrsList}">
				<tr>
				    <td><input type="checkbox" value="${prs.id}" class="check"/></td>
					<td><c:out value="${prs.uuid}" /></td>
					<td><c:out value="${prs.hospital_name}" /></td>
					<td><c:out value="${prs.outer_id}" /></td>
					<td><c:out value="${prs.patient_name}" /></td>
					<td><c:out value="${prs.packet_num}" /></td>
					<td>完成</td>
					<td><c:out value="${prs.finish_time}"/></td>
					<td width="100">
						<div class="btn-group" role="group" aria-label="...">
							<% if (request.getParameter("pageNum") == null){ %>
							<% 		request.setAttribute("page_num", 1); %>
							<% }else{ %>
							<% 		request.setAttribute("page_num", request.getParameter("pageNum")); %>
							<% } %>
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/process/showAllProcs?prsId=${prs.id}&from=HISTORY">状态</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<% if (request.getAttribute("hospitalId") != null){ %>
		<% request.setAttribute("hospitalId", (Integer)request.getAttribute("hospitalId")); %>
	<% }%>
	<% if (request.getAttribute("startTime") != null){ %>
		<% request.setAttribute("startTime", (String)request.getAttribute("startTime")); %>
	<% } %>
	<% if (request.getAttribute("endTime") != null){ %>
		<% request.setAttribute("endTime", (String)request.getAttribute("endTime")); %>
	<% } %>
	<c:set var="pageUrl" value="prescription/historyList" />
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
var deletePrsSelected = function(hospitalId, startTime, endTime){
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
		$("#assureMsg").html("确认删除标签?");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/deletePrsSelected?hospitalId="+hospitalId+"&prsList="+prsListStr+"&startTime="+startTime+"&endTime="+endTime+"&pageFrom=historyList");
        $("#assureDlg").modal("show");
	}
}
</script>
<%@ include file="../foot.jsp"%>