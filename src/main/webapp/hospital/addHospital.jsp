<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.bean.Hospital" %>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">添加医院</h3>
<div>
	<div>
		<c:if test="${not empty errorMsg}">
			<div class="alert alert-danger" role="alert">${errorMsg}</div>
		</c:if>
		<c:if test="${not empty successMsg}">
			<div class="alert alert-success" role="alert">${successMsg}</div>
		</c:if>
	</div>
	<form class="form-horizontal" action="<%=request.getContextPath()%>/hospital/add" method="POST">
		<fieldset>
			<div class="form-group">
				<label for="name" class="control-label col-sm-4">医院名称</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="name" name="name" placeholder="医院名称" value="${hospitalAdd.name}" required maxlength="100">
				</div>
			</div>
			<div class="form-group">
				<label for="description" class="control-label col-sm-4">医院简介</label>
				<div class="col-sm-8">
					<textarea class="form-control" rows="5" id="description" name="description" placeholder="医院简介">${hospitalAdd.description}</textarea>
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