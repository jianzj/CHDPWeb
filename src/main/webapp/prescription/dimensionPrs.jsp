<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>

<h3 class="sub-header">
	处方汇总列表
</h3>
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
				<th>结束时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="prs" items="${finishPrsList}">
				<tr>
					<td><c:out value="${prs.id}" /></td>
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
							 <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/process/showAllProcs?prsId=${prs.id}&from=${from}">状态</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<% if (request.getAttribute("hospital") != null){ %>
		<% request.setAttribute("hospital", (String)request.getAttribute("hospital")); %>
	<% } %>
	<% if (request.getAttribute("startTime") != null){ %>
		<% request.setAttribute("startTime", (String)request.getAttribute("startTime")); %>
	<% }else{ %>
		<% request.setAttribute("startTime", Constants.DEFAULT_START ); %>
	<% } %>
	<% if (request.getAttribute("endTime") != null){ %>
		<% request.setAttribute("endTime", (String)request.getAttribute("endTime")); %>
	<% }else{ %>
		<% request.setAttribute("endTime", Constants.DEFAULT_END); %>
	<% } %>
	<% if (request.getAttribute("userId") != null){ %>
		<% request.setAttribute("userId", (Integer)request.getAttribute("userId")); %>
	<% }else{ %>
		<% request.setAttribute("userId", 0); %>
	<% } %>
	<% if (request.getAttribute("orderId") != null){ %>
		<% request.setAttribute("orderId", (Integer)request.getAttribute("orderId")); %>
	<% }else{ %>
		<% request.setAttribute("orderId", 1); %>
	<% } %>
	<c:set var="pageUrl" value="prescription/dimensionPrsList" />
	<%@ include file="../common/nav.jsp"%>
</div>
<%@ include file="../foot.jsp"%>