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
	<form class="form-horizontal" action="<%=request.getContextPath()%>/prescription/modify?prsId=${prsModify.id}&from=${from}" method="POST">
		<fieldset>
			<div class="form-group">
				<label for="hospital_id" class="control-label col-sm-4">医院</label>
				<div class="col-sm-4 required">
					<% if(request.getAttribute("hospitalList") != null){ %>
					<select class="selectpicker" data-live-search="true" data-width="fit" id="hospital_id" name="hospital_id">
						<c:forEach var='item' items="${hospitalList}">
								<c:if test="${prsModify.hospital_id == item.id}">
									<option value="${item.id}" selected>${item.name}</option>						
								</c:if>
								<c:if test="${prsModify.hospital_id != item.id}">
									<option value="${item.id}">${item.name}</option>						
								</c:if>
						</c:forEach>
					</select>
					<% }else{ %>
					<div class="alert alert-danger" role="alert">暂无医院信息，请联系管理员录入相关医院</div>
					<% } %>
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
				<div class="col-sm-5">
					<label><input name="packet_num" type="radio" value="5" <%if(((Prescription)request.getAttribute("prsModify")).getPacket_num() == 5) out.print("checked"); %>>5帖10包</label>　　
                    <label><input name="packet_num" type="radio" value="7" <%if(((Prescription)request.getAttribute("prsModify")).getPacket_num() == 7) out.print("checked"); %>>7帖14包</label>　　
                    <label><input name="packet_num" type="radio" value="10" <%if(((Prescription)request.getAttribute("prsModify")).getPacket_num() == 10) out.print("checked"); %>>10帖20包</label>　　
                    <label><input name="packet_num" type="radio" value="14" <%if(((Prescription)request.getAttribute("prsModify")).getPacket_num() == 14) out.print("checked"); %>>14帖28包</label>　　
                    <div class="input-group" style="margin-top:10px;">
                      <span class="input-group-addon">
                        <label><input name="packet_num" type="radio" value="-1" <% int n = ((Prescription)request.getAttribute("prsModify")).getPacket_num(); if(n!=5&&n!=7&&n!=10&&n!=14) out.print("checked"); %>>其它</label>
                      </span>
                      <input type="number" step="1" class="form-control" name="packet_num_other"  value="<% if(n!=5&&n!=7&&n!=10&&n!=14) out.print(n); %>">
                    </div>
				</div>
			</div>
			<div class="form-group">
				<label for="price_num" class="control-label col-sm-4">金额</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="price_num" name="price_num" placeholder="价格" value="${prsModify.price}" required>
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