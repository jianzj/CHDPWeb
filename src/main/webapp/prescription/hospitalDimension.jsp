<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>

<form class="form-inline" action="<%=request.getContextPath()%>/prescription/hospitalDimensionList" method="GET">

<h3 class="sub-header">
	当前处方列表
	<span>
		<select class="selectpicker" data-live-search="true" data-width="fit" id="hospital" name="hospital">
			<option value="ALL">全部医院</option>
			
			<c:forEach var='hospital' items="${hospitalList}">
				<option value="${hospital.name}">${hospital.name}</option>
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
				<th>已完成处方</th>
				<th>始于</th>
				<th>止于</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="hospital" items="${hospitalList}">
				<tr>
					<td><c:out value="${hospital.id}" /></td>
					<td><c:out value="${hospital.name}" /></td>
					<td><c:out value="${hospital.finishedPrsNum}" /></td>
					<td><c:out value="${start}" /></td>
					<td><c:out value="${end}" /></td>
					<td width="100">
						<div class="btn-group" role="group" aria-label="...">
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/prescription/prescriptionList?hospital=${hospital}&start=${start}&end=${end}&from=hospitalDimension">处方详情</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@ include file="../foot.jsp"%>