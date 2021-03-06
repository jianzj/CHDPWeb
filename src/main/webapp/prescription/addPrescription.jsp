<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.bean.Prescription" %>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">添加处方</h3>
<div>
	<div>
		<c:if test="${not empty errorMsg}">
			<div class="alert alert-danger" role="alert">${errorMsg}</div>
		</c:if>
		<c:if test="${not empty successMsg}">
			<div class="alert alert-success" role="alert">${successMsg}</div>
		</c:if>
	</div>
	<form class="form-horizontal" action="<%=request.getContextPath()%>/prescription/add" method="POST">
		<fieldset>
			<div class="form-group">
				<label for="hospital_id" class="control-label col-sm-4">医院</label>
				<div class="col-sm-4 required">
					<% if(request.getAttribute("hospitalList") != null){ %>
					<select class="selectpicker" data-live-search="true" data-width="fit" id="hospital_id" name="hospital_id">
						<% if(request.getAttribute("prsAdd") != null) { %>
						<c:forEach var='hospital' items="${hospitalList}">
							<c:if test="${prsAdd.hospital_id == hospital.id}">
								<option value="${hospital.id}" selected>${hospital.name}</option>						
							</c:if>
							<c:if test="${prsAdd.hospital_id != hospital.id}">
								<option value="${hospital.id}">${hospital.name}</option>						
							</c:if>
						</c:forEach>
						<% } else if (request.getAttribute("lastestPrs") != null){ %>
						<c:forEach var='hospital' items="${hospitalList}">
							<c:if test="${lastestPrs.hospital_id == hospital.id}">
								<option value="${hospital.id}" selected>${hospital.name}</option>						
							</c:if>
							<c:if test="${lastestPrs.hospital_id != hospital.id}">
								<option value="${hospital.id}">${hospital.name}</option>						
							</c:if>
						</c:forEach>				
						<% } else { %>
						<c:forEach var='hospital' items="${hospitalList}">
							<option value="${hospital.id}">${hospital.name}</option>
						</c:forEach>
						<% } %>
					</select>
					<% }else{ %>
					<div class="alert alert-danger" role="alert">暂无医院信息，请联系管理员录入相关医院</div>
					<% } %>
				</div>
			</div>
			<div class="form-group">
				<label for="outer_id" class="control-label col-sm-4">医院处方登记编号</label>
				<div class="col-sm-4">
					<% if(request.getAttribute("lastestPrs") != null && request.getAttribute("prsAdd") == null){ %>
						<input type="text" class="form-control" id="outer_id" name="outer_id" placeholder="院方编号" value="${lastestPrs.outer_id}" required>
					<% }else {%>
					    <input type="text" class="form-control" id="outer_id" name="outer_id" placeholder="院方编号" value="${prsAdd.outer_id}" required>
					<% } %>
				</div>
			</div>
			<div class="form-group">
				<label for="patient_name" class="control-label col-sm-4">姓名</label>
				<div class="col-sm-4">
					<% if(request.getAttribute("lastestPrs") != null && request.getAttribute("prsAdd") == null){ %>
					<input type="text" class="form-control" id="patient_name" name="patient_name" placeholder="姓名" value="${lastestPrs.patient_name}" required maxlength="20">
					<% }else {%>
					<input type="text" class="form-control" id="patient_name" name="patient_name" placeholder="姓名" value="${prsAdd.patient_name}" required maxlength="20">
					<% } %>
				</div>
			</div>
			<div class="form-group">
				<label for="sex" class="control-label col-sm-4">性别</label>
				<div class="col-sm-4 required">
					<select class="form-control col-sm-4" id="sex" name="sex">
						<% if(request.getAttribute("lastestPrs") != null && request.getAttribute("prsAdd") == null){ %>
						<option value="<%=Constants.MAN %>" <%if(request.getAttribute("lastestPrs")!=null && ((Prescription)request.getAttribute("lastestPrs")).getSex() == 1) out.print("selected"); %>>男</option>
						<option value="<%=Constants.WOMAN %>" <%if(request.getAttribute("lastestPrs")!=null && ((Prescription)request.getAttribute("lastestPrs")).getSex() == 2) out.print("selected"); %>>女</option>
						<% }else {%>
						<option value="<%=Constants.MAN %>" <%if(request.getAttribute("prsAdd")!=null && ((Prescription)request.getAttribute("prsAdd")).getSex() == 1) out.print("selected"); %>>男</option>
						<option value="<%=Constants.WOMAN %>" <%if(request.getAttribute("prsAdd")!=null && ((Prescription)request.getAttribute("prsAdd")).getSex() == 2) out.print("selected"); %>>女</option>
						<% } %>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="packet_num" class="control-label col-sm-4">帖数</label>                        
				<div class="col-sm-5">
					<% if(request.getAttribute("lastestPrs") != null && request.getAttribute("prsAdd") == null){ %>
					<label><input name="packet_num" type="radio" value="5" <%if(((Prescription)request.getAttribute("lastestPrs")).getPacket_num() == 5) out.print("checked"); %>>5帖10包</label>　　
					<label><input name="packet_num" type="radio" value="7" <%if(((Prescription)request.getAttribute("lastestPrs")).getPacket_num() == 7) out.print("checked"); %>>7帖14包</label>　　
                    <label><input name="packet_num" type="radio" value="10" <%if(((Prescription)request.getAttribute("lastestPrs")).getPacket_num() == 10) out.print("checked"); %>>10帖20包</label>　　
                    <label><input name="packet_num" type="radio" value="14" <%if(((Prescription)request.getAttribute("lastestPrs")).getPacket_num() == 14) out.print("checked"); %>>14帖28包</label>　　
                    <div class="input-group" style="margin-top:10px;">
				      <span class="input-group-addon">
				        <label><input name="packet_num" type="radio" value="-1" <% int n = ((Prescription)request.getAttribute("lastestPrs")).getPacket_num(); if(n!=5&&n!=7&&n!=10&&n!=14) out.print("checked"); %>>其它</label>
				      </span>
				      <input type="number" step="1" class="form-control" name="packet_num_other"  value="<% if(n!=5&&n!=7&&n!=10&&n!=14) out.print(n); %>">
				    </div>
					<% }else if (request.getAttribute("prsAdd")!=null) {%>
					<label><input name="packet_num" type="radio" value="5" <%if(((Prescription)request.getAttribute("prsAdd")).getPacket_num() == 5) out.print("checked"); %>>5帖10包</label>　　
                    <label><input name="packet_num" type="radio" value="7" <%if(((Prescription)request.getAttribute("prsAdd")).getPacket_num() == 7) out.print("checked"); %>>7帖14包</label>　　
                    <label><input name="packet_num" type="radio" value="10" <%if(((Prescription)request.getAttribute("prsAdd")).getPacket_num() == 10) out.print("checked"); %>>10帖20包</label>　　
                    <label><input name="packet_num" type="radio" value="14" <%if(((Prescription)request.getAttribute("prsAdd")).getPacket_num() == 14) out.print("checked"); %>>14帖28包</label>　　
                    <div class="input-group" style="margin-top:10px;">
                      <span class="input-group-addon">
                        <label><input name="packet_num" type="radio" value="-1" <% int n = ((Prescription)request.getAttribute("prsAdd")).getPacket_num(); if(n!=5&&n!=7&&n!=10&&n!=14) out.print("checked"); %>>其它</label>
                      </span>
                      <input type="number" step="1" class="form-control" name="packet_num_other"  value="<% if(n!=5&&n!=7&&n!=10&&n!=14) out.print(n); %>">
                    </div>
					<% }else{ %>
					<label><input name="packet_num" type="radio" value="5" checked>5帖10包</label>　　
                    <label><input name="packet_num" type="radio" value="7">7帖14包</label>　　
                    <label><input name="packet_num" type="radio" value="10">10帖20包</label>　　
                    <label><input name="packet_num" type="radio" value="14">14帖28包</label>　　
                    <div class="input-group" style="margin-top:10px;">
                      <span class="input-group-addon">
                        <label><input name="packet_num" type="radio" value="-1">其它</label>
                      </span>
                      <input type="number" step="1" class="form-control" name="packet_num_other">
                    </div>
					<% } %>
				</div>
			</div>
			<div class="form-group">
				<label for="price" class="control-label col-sm-4">金额</label>
				<div class="col-sm-4">
					<% if(request.getAttribute("lastestPrs") != null && request.getAttribute("prsAdd") == null){ %>
					<input type="number" step="0.01" class="form-control" id="prs_price" name="prs_price" placeholder="价格" value="${lastestPrs.price}" required>
					<% }else {%>
					<input type="number" step="0.01" class="form-control" id="prs_price" name="prs_price" placeholder="价格" value="${prsAdd.price}" required>
					<% } %>
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