<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.bean.Herb" %>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">添加中药</h3>
<div>
	<div>
		<c:if test="${not empty errorMsg}">
			<div class="alert alert-danger" role="alert">${errorMsg}</div>
		</c:if>
		<c:if test="${not empty successMsg}">
			<div class="alert alert-success" role="alert">${successMsg}</div>
		</c:if>
	</div>
	<form class="form-horizontal" action="<%=request.getContextPath()%>/herb/add" method="POST">
		<fieldset>
			<div class="form-group">
				<label for="type" class="control-label col-sm-4">中药类型</label>
				<div class="col-sm-4 required">
					<select class="form-control col-sm-4" id="type" name="type">
						<option value="<%=Constants.DECOCT_FIRST %>" <%if(request.getAttribute("herbAdd")!=null && ((Herb)request.getAttribute("herbAdd")).getType() == 1) out.print("selected"); %>>先煎</option>
						<option value="<%=Constants.DECOCT_LATER %>" <%if(request.getAttribute("herbAdd")!=null && ((Herb)request.getAttribute("herbAdd")).getType() == 2) out.print("selected"); %>>后下</option>
						<option value="<%=Constants.WRAPPED_DECOCT %>" <%if(request.getAttribute("herbAdd")!=null && ((Herb)request.getAttribute("herbAdd")).getType() == 3) out.print("selected"); %>>包煎</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="control-label col-sm-4">中药名称</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="name" name="name" placeholder="中药名称" value="${herbAdd.name}" required maxlength="20">
				</div>
			</div>
			<div class="form-group">
				<label for="description" class="control-label col-sm-4">中药描述</label>
				<div class="col-sm-6">
				    <textarea class="form-control" rows="5" id="description" name="description" placeholder="中药描述">${herbAdd.description}</textarea>
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