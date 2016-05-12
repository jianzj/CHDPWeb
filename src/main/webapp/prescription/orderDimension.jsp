<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>

<form class="form-inline" action="<%=request.getContextPath()%>/prescription/orderDimensionList" method="GET">

<h3 class="sub-header">
	出货单维度统计
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
		<span class="input-group input-append date col-xs-2" id="hospital-datePicker-start">
                <input type="text" class="form-control" name="startTime" value="${startTime}"/>
                <span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
         </span>
         <span class="input-group input-append date col-xs-2" id="hospital-datePicker-end">
                <input type="text" class="form-control" name="endTime" value="${endTime}"/>
         <span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
         </span>
         <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
         <a type="button" class="btn btn-success" style="" onClick="exportList(${hospitalId}, '${startTime}', '${endTime}')">导出出货单统计清单</a>
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
				<th>出货单编号</th>
				<th>医院</th>
				<th>创建人</th>
				<th>出库人</th>
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
					<td><c:out value="${order.outbound_user_name}" /></td>
					<td><c:out value="${order.prs_num}" /></td>
					<td><c:out value="${order.packet_num}" /></td>
					<td><c:out value="${order.price_total}" /></td>
					<c:if test="${order.prs_num > 0}">
					<td width="200">
						<div class="btn-group" role="group" aria-label="...">
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/prescription/dimensionPrsList?startTime=${startTime}&endTime=${endTime}&orderId=${order.id}&userId=&hospitalId=&from=ORDER">处方详情</a>
						</div>
						<div class="btn-group" role="group" aria-label="...">
						    <a type="button" class="btn btn-success" style="" onClick="generateOrderList(${order.id}, ${hospitalId}, '${startTime}', '${endTime}')">打印出库单</a>
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
	<% if (request.getAttribute("startTime") != null){ %>
		<% request.setAttribute("startTime", (String)request.getAttribute("startTime")); %>
	<% } %>
	<% if (request.getAttribute("endTime") != null){ %>
		<% request.setAttribute("endTime", (String)request.getAttribute("endTime")); %>
	<% }%>
	<c:set var="pageUrl" value="prescription/orderDimensionList" />
	<%@ include file="../common/nav.jsp"%>
</div>

<div class="modal fade" id="assureDlg" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-body" id="assureMsg"></div>
			<div class="modal-footer">
				<a type="button" class="btn btn-success" id="assureBtn" target="_blank">确认</a> <a type="button" class="btn btn-default"
					data-dismiss="modal">取消</a>
			</div>
		</div>
	</div>
</div>
<script>
    var exportList = function(hospitalId, startTime, endTime){
    	$("#assureMsg").html("确认导出出货单维度统计单?");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/printOrderDimensionXls?hospitalId="+hospitalId+"&startTime="+startTime+"&endTime="+endTime);
        $("#assureDlg").modal("show");
    };
    
    var generateOrderList = function(orderId, hospitalId, startTime, endTime){
    	$("#assureMsg").html("确认重新打印此出库单单?");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/regenerateShipListXls?orderId="+orderId+"&shospitalId="+hospitalId+"&startTime="+startTime+"&endTime="+endTime);
        $("#assureDlg").modal("show");
    };
</script>
<%@ include file="../foot.jsp"%>