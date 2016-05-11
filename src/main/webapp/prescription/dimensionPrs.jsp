<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ page import="com.chdp.chdpweb.common.Utils" %>
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
				<th>操作</th>
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
	<% if (request.getAttribute("hospitalId") != null){ %>
		<% request.setAttribute("hospitalId", (Integer)request.getAttribute("hospitalId")); %>
	<% } %>
	<% if (request.getAttribute("startTime") != null){ %>
		<% request.setAttribute("startTime", (String)request.getAttribute("startTime")); %>
	<% } %>
	<% if (request.getAttribute("endTime") != null){ %>
		<% request.setAttribute("endTime", (String)request.getAttribute("endTime")); %>
	<% } %>
	<% if (request.getAttribute("userId") != null){ %>
		<% request.setAttribute("userId", (Integer)request.getAttribute("userId")); %>
	<% } %>
	<% if (request.getAttribute("userAuth") != null){ %>
		<% request.setAttribute("userAuth", (Integer)request.getAttribute("userAuth")); %>
	<% } %>
	<% if (request.getAttribute("orderId") != null){ %>
		<% request.setAttribute("orderId", (Integer)request.getAttribute("orderId")); %>
	<% } %>
	<% if (request.getAttribute("from") != null){ %>
		<% request.setAttribute("from", (String)request.getAttribute("from")); %>
	<% } %>
	<c:set var="pageUrl" value="prescription/dimensionPrsList" />
	<%@ include file="../common/nav.jsp"%>
</div>
<%@ include file="../foot.jsp"%>