<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.bean.Prescription" %>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>

<h3 class="sub-header">修改处方</h3>
<div>
	<div>
		<c:if test="${not empty errorMsg}">
			<div class="alert alert-danger" role="alert">${errorMsg}</div>
		</c:if>
		<c:if test="${not empty successMsg}">
			<div class="alert alert-success" role="alert">${successMsg}</div>
		</c:if>
	</div>
	<form class="form-horizontal" action="<%=request.getContextPath()%>/prescription/shipModify?prsId=${prsModify.id}" method="POST">
		<fieldset>
			<div class="form-group">
				<label for="hospital_name" class="control-label col-sm-4">医院</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="hospital_name" name="hospital_name" placeholder="医院名称" value="${prsModify.hospital_name}" required>
				</div>
			</div>
			<div class="form-group">
				<label for="outer_id" class="control-label col-sm-4">医院处方登记编号</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="outer_id" name="outer_id" placeholder="院方编号" value="${prsModify.outer_id}" required>
				</div>
			</div>
			<div class="form-group">
				<label for="patient_name" class="control-label col-sm-4">姓名</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="patient_name" name="patient_name" placeholder="姓名" value="${prsModify.patient_name}" required maxlength="20">
				</div>
			</div>
			<div class="form-group">
				<label for="sex" class="control-label col-sm-4">性别</label>
				<div class="col-sm-4 required">
					<select class="form-control col-sm-4" id="sex" name="sex">
						<option value="<%=Constants.MAN %>" <%if(request.getAttribute("prsModify")!=null && ((Prescription)request.getAttribute("prsModify")).getSex() == 1) out.print("selected"); %>>男</option>
						<option value="<%=Constants.WOMAN %>" <%if(request.getAttribute("prsModify")!=null && ((Prescription)request.getAttribute("prsModify")).getSex() == 2) out.print("selected"); %>>女</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="packet_num" class="control-label col-sm-4">帖数</label>
				<div class="col-sm-4">
					<input type="number" class="form-control" id="packet_num" name="packet_num" placeholder="贴数" value="${prsModify.packet_num}" required>
				</div>
			</div>
			<div class="form-group">
				<label for="price" class="control-label col-sm-4">金额</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="price" name="price" placeholder="价格" value="${prsModify.price}" required>
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