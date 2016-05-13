<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.common.Utils" %>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>

<form class="form-inline" action="<%=request.getContextPath()%>/prescription/hospitalDimensionList" method="GET">
<h3 class="sub-header">
	医院维度统计
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
         <a type="button" class="btn btn-success" style="" href="<%=request.getContextPath()%>/prescription/printHospitalDimensionXls?hospitalId=${hospitalId}&startTime=${startTime}&endTime=${endTime}">导出医院统计清单</a>
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
				<th>医院</th>
				<th>出库单数</th>
				<th>处方数</th>
				<th>贴数</th>
				<th>总计</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="hosp2" items="${displayHospitalList}">
				<tr>
					<td><c:out value="${hosp2.name}" /></td>
					<td><c:out value="${hosp2.orderNum}" /></td>
					<td><c:out value="${hosp2.finishedPrsNum}" /></td>
					<td><c:out value="${hosp2.totalPacketNum}" /></td>
					<td><c:out value="${hosp2.totalPrice}" /></td>
					<c:if test="${hosp2.finishedPrsNum > 0}">
					<td width="100">
						<div class="btn-group" role="group" aria-label="...">
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/prescription/dimensionPrsList?startTime=${startTime}&endTime=${endTime}&userId=&hospitalId=${hosp2.id}&from=HOSPITAL" target="_blank">处方详情</a>
						</div>
					</td>
					</c:if>
					<c:if test="${hosp2.finishedPrsNum == 0}">
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
    <c:set var="pageUrl" value="prescription/hospitalDimensionList" />
    <%@ include file="../common/nav.jsp"%>
</div>
<script>
$(".selectpicker").change(function(){
    $(".form-inline").submit();
}); 
</script>
<%@ include file="../foot.jsp"%>