<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">修改密码</h3>
<div>
	<div>
		<c:if test="${not empty errorMsg}">
			<div class="alert alert-danger" role="alert">${errorMsg}</div>
		</c:if>
		<c:if test="${not empty successMsg}">
			<div class="alert alert-success" role="alert">${successMsg}</div>
		</c:if>
	</div>
	<form class="form-horizontal" action="<%=request.getContextPath()%>/user/changePassword" method="POST">
		<fieldset>
			<div class="form-group">
				<label for="password" class="control-label col-sm-4">当前密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="password" name="password" placeholder="当前密码" required>
				</div>
			</div>
			<div class="form-group">
				<label for="newPassword" class="control-label col-sm-4">新密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="newPassword" name="newPassword" placeholder="新密码" required>
				</div>
			</div>
			<div class="form-group">
				<label for="renewPassword" class="control-label col-sm-4">确认密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="renewPassword" name="renewPassword" placeholder="确认密码" required>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-4"></label>
				<div class="col-sm-4">
					<button class="btn btn-primary" type="submit">提交</button>
				</div>
			</div>
		</fieldset>
	</form>

</div>
<%@ include file="../foot.jsp"%>