<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
				<th>已完成处方</th>
				<th>贴数</th>
				<th>总计</th>
				<th>始于</th>
				<th>止于</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="hosp2" items="${displayHospitalList}">
				<tr>
					<td><c:out value="${hosp2.id}" /></td>
					<td><c:out value="${hosp2.name}" /></td>
					<td><c:out value="${hosp2.finishedPrsNum}" /></td>
					<td><c:out value="${hosp2.totalPacketNum}" /></td>
					<td><c:out value="${hosp2.totalPrice}" /></td>
					<td><c:out value="${startTime}" /></td>
					<% if (request.getAttribute("endTime") == null || ((String)request.getAttribute("endTime")).equals(Constants.DEFAULT_END)){ %>
					<td><%=Constants.getCurrentTime() %></td>
					<% }else{ %>
					<td><c:out value="${endTime}" /></td>
					<% } %>
					<c:if test="${hosp2.finishedPrsNum > 0}">
					<td width="100">
						<div class="btn-group" role="group" aria-label="...">
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/prescription/dimensionPrsList?startTime=${startTime}&endTime=${endTime}&userId=&hospitalId=${hosp2.id}&from=HOSPITAL">处方详情</a>
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
<%@ include file="../foot.jsp"%>