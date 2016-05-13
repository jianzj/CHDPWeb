<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>

<form class="form-inline" action="<%=request.getContextPath()%>/order/currentOrders" method="GET">
<h3 class="sub-header">
	当前出库单列表
	<span>
		<select class="selectpicker" data-live-search="true" data-width="fit" id="hospitalId" name="hospitalId">
			<option value=0>全部医院</option>
			
			<c:forEach var='hosp1' items="${hospitalList}">
				<c:if test="${hospitalId == hosp1.id}">
				<option value="${hosp1.id}" selected>${hosp1.name}</option>
				</c:if>
				<c:if test="${hospitalId != hosp1.id}">
				<option value="${hosp1.id}">${hosp1.name}</option>
				</c:if>
			</c:forEach>
		</select>
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
				<th>出库单编号</th>
				<th>医院</th>
				<th>创建人</th>
				<th>处方数</th>
				<th>贴数</th>
				<th>金额</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="order" items="${currentOrderList}">
				<tr>
					<td><c:out value="${order.uuid}" /></td>
					<td><c:out value="${order.hospital_name}" /></td>
					<td><c:out value="${order.create_user_name}" /></td>
					<td><c:out value="${order.prs_num}" /></td>
					<td><c:out value="${order.packet_num}" /></td>
					<td><c:out value="${order.price_total}" /></td>
					<c:if test="${order.prs_num > 0}">
					<td width="200">
						<div class="btn-group" role="group" aria-label="...">
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/prescription/dimensionPrsList?startTime=&endTime=&orderId=${order.id}&userId=&hospitalId=&from=CURRENT_ORDER">处方详情</a>
						</div>
						<div class="btn-group" role="group" aria-label="...">
						    <a type="button" class="btn btn-success" href="<%=request.getContextPath()%>/prescription/regenerateShipListXls?orderId=${order.id}&shospitalId=${hospitalId}" target="_blank">下载出库单</a>
						</div>
					</td>
					</c:if>
					<c:if test="${order.prs_num == 0}">
					<td width="100">
						<div class="btn-group disabled" role="group" aria-label="...">
						    <a type="button" class="btn btn-default" href="#">处方详情</a>
						</div>
					</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<% if (request.getAttribute("hospitalId") != null){ %>
		<% request.setAttribute("hospitalId", (Integer)request.getAttribute("hospitalId")); %>
	<% } %>
	<c:set var="pageUrl" value="order/currentOrders" />
	<%@ include file="../common/nav.jsp"%>
</div>
<script>
$(".selectpicker").change(function(){
    $(".form-inline").submit();
}); 
</script>
<%@ include file="../foot.jsp"%>