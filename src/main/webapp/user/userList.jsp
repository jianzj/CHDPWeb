<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">用户列表</h3>
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
				<th>工号</th>
				<th>姓名</th>
				<th>权限</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${userList}">
				<tr>
					<td><c:out value="${user.id}" /></td>
					<td><c:out value="${user.usercode}" /></td>
					<td><c:out value="${user.name}" /></td>
					<td><c:out value="${user.authority}" /></td>
					<td width="300">
						<div class="btn-group" role="group" aria-label="...">
							<a type="button" class="btn btn-primary" href="">权限配置</a>
							<a type="button" class="btn btn-primary" href="">重置密码</a>
							<a type="button" class="btn btn-primary" href="">删除用户</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<c:set var="pageUrl" value="user/list" />
	<%@ include file="../common/nav.jsp"%>
</div>
<%@ include file="../foot.jsp"%>