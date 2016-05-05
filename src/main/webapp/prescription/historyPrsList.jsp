<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>

<form class="form-inline" action="<%=request.getContextPath()%>/prescription/historyList" method="GET">
<h3 class="sub-header">
	历史处方列表
	<span>
		<select class="selectpicker" data-live-search="true" data-width="fit" id="hospital" name="hospital">
			<option value="ALL">全部医院</option>
			
			<c:forEach var='hosp' items="${hospitalList}">
				<option value="${hosp.name}">${hosp.name}</option>
			</c:forEach>
		</select>
		<span class="input-group input-append date col-xs-2" id="hospital-datePicker-start">
                <input type="text" class="form-control" name="start"/>
                <span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
         </span>
         <span class="input-group input-append date col-xs-2" id="hospital-datePicker-end">
                <input type="text" class="form-control" name="end"/>
         <span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
         </span>
		<button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
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
				<th>ID</th>
				<th>医院</th>
				<th>医院编号</th>
				<th>姓名</th>
				<th>贴数</th>
				<th>当前状态</th>
				<th>处理人员</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="prs" items="${historyPrsList}">
				<tr>
					<td><c:out value="${prs.id}" /></td>
					<td><c:out value="${prs.hospital_name}" /></td>
					<td><c:out value="${prs.outer_id}" /></td>
					<td><c:out value="${prs.patient_name}" /></td>
					<td><c:out value="${prs.packet_num}" /></td>
					<td>完成</td>
					<td><c:out value="${prs.user_name}" /></td>
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
	<% request.setAttribute("hospital", (String)request.getAttribute("hospital")); %>
	<% request.setAttribute("start", (String)request.getAttribute("start")); %>
	<% request.setAttribute("end", (String)request.getAttribute("end")); %>
	<c:set var="pageUrl" value="prescription/historyList" />
	<%@ include file="../common/nav.jsp"%>
</div>
<%@ include file="../foot.jsp"%>