<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>

<form class="form-inline" action="<%=request.getContextPath()%>/prescription/userDimensionList" method="GET">

<h3 class="sub-header">
	员工维度统计
	<span>
		<select class="selectpicker" data-live-search="true" data-width="fit" id="userAuth" name="userAuth">
			<option value=0>全部用户</option>
			<c:if test="${userAuth == 512}">
			<option value=512 selected>接方</option>
			</c:if>
			<c:if test="${userAuth != 512}">
			<option value=512>接方</option>
			</c:if>
			<c:if test="${userAuth == 256}">
			<option value=256 selected>审方</option>
			</c:if>
			<c:if test="${userAuth != 256}">
			<option value=256>审方</option>
			</c:if>
			<c:if test="${userAuth == 128}">
			<option value=128 selected>调配</option>
			</c:if>
			<c:if test="${userAuth != 128}">
			<option value=128>调配</option>
			</c:if>
			<c:if test="${userAuth == 64}">
			<option value=64 selected>调配审核</option>
			</c:if>
			<c:if test="${userAuth != 64}">
			<option value=64>调配审核</option>
			</c:if>
			<c:if test="${userAuth == 32}">
			<option value=32 selected>浸泡</option>
			</c:if>
			<c:if test="${userAuth != 32}">
			<option value=32>浸泡</option>
			</c:if>
			<c:if test="${userAuth == 16}">
			<option value=16 selected>煎煮</option>
			</c:if>
			<c:if test="${userAuth != 16}">
			<option value=16>煎煮</option>
			</c:if>
			<c:if test="${userAuth == 8}">
			<option value=8 selected>灌装</option>
			</c:if>
			<c:if test="${userAuth != 8}">
			<option value=8>灌装</option>
			</c:if>
			<c:if test="${userAuth == 4}">
			<option value=4 selected>清场</option>
			</c:if>
			<c:if test="${userAuth != 4}">
			<option value=4>清场</option>
			</c:if>
			<c:if test="${userAuth == 2}">
			<option value=2 selected>包装</option>
			</c:if>
			<c:if test="${userAuth != 2}">
			<option value=2>包装</option>
			</c:if>
			<c:if test="${userAuth == 1}">
			<option value=1 selected>配送</option>
			</c:if>
			<c:if test="${userAuth != 1}">
			<option value=1>配送</option>
			</c:if>
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
				<th>工号</th>
				<th>姓名</th>
				<th>职位</th>
				<th>完成</th>
				<th>出错</th>
				<th>始于</th>
				<th>止于</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${finalUserList}">
				<tr>
					<td><c:out value="${user.usercode}" /></td>
					<td><c:out value="${user.name}" /></td>
					<td><c:out value="${user.position}"/></td>
					<td><c:out value="${user.done_prs_num}" /></td>
					<td><c:out value="${user.error_num}" /></td>
					<% if(request.getAttribute("startTime") == null || ((String)request.getAttribute("startTime")).equals("")){ %>
					<td><%=Constants.DEFAULT_START %></td>
					<% }else{ %>
					<td><c:out value="${startTime}" /></td>
					<% } %>
					<% if (request.getAttribute("endTime") == null || ((String)request.getAttribute("endTime")).equals(Constants.DEFAULT_END)){ %>
					<td><%=Constants.getCurrentTime() %></td>
					<% }else{ %>
					<td><c:out value="${endTime}" /></td>
					<% } %>
					<c:if test="${user.done_prs_num > 0}">
					<td width="100">
						<div class="btn-group" role="group" aria-label="...">
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/prescription/dimensionPrsList?startTime=${startTime}&endTime=${endTime}&userAuth=${userAuth}&userId=${user.id}&hospitalId=&from=USER">处方详情</a>
						</div>
					</td>
					</c:if>
					<c:if test="${user.done_prs_num == 0}">
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