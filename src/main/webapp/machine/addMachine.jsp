<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.bean.Machine" %>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">添加机器</h3>
<div>
	<div>
		<c:if test="${not empty errorMsg}">
			<div class="alert alert-danger" role="alert">${errorMsg}</div>
		</c:if>
		<c:if test="${not empty successMsg}">
			<div class="alert alert-success" role="alert">${successMsg}</div>
		</c:if>
	</div>
	<form class="form-horizontal" action="<%=request.getContextPath()%>/machine/add" method="POST">
		<fieldset>
			<div class="form-group">
				<label for="type" class="control-label col-sm-4">机器类型</label>
				<div class="col-sm-4 required">
					<select id="machine_type" class="form-control col-sm-4" id="type" name="type">
						<option value="<%=Constants.DECOCTION_MACHINE %>" <%if(request.getAttribute("machineAdd")!=null && ((Machine)request.getAttribute("machineAdd")).getType() == 1) out.print("selected"); %>>煎煮机</option>
						<option value="<%=Constants.FILLING_MACHINE %>" <%if(request.getAttribute("machineAdd")!=null && ((Machine)request.getAttribute("machineAdd")).getType() == 2) out.print("selected"); %>>灌装机</option>
					</select>
				</div>
			</div>
			<div id="pour_machine">
				<div class="form-group">
					<label for="pour_machine_id" class="control-label col-sm-4">关联的灌装机</label>
					<div class="col-sm-4 required">
						<select class="form-control col-sm-4" id="pour_machine_id" name="pour_machine_id">
							<option value=0>未选择关联的灌装机</option>
							<% if (request.getAttribute("pourMachineList") != null){ %>
							<c:forEach var="fill_machine" items="${pourMachineList}">
								<% if (request.getAttribute("machineAdd") != null){ %>
								<c:if test="${machineAdd.pour_machine_id == fill_machine.id}">
								<option value="${fill_machine.id}" selected>${fill_machine.name}</option>
								</c:if>
								<c:if test="${machineAdd.pour_machine_id != fill_machine.id}">
								<option value="${fill_machine.id}">${fill_machine.name}</option>
								</c:if>
								<% }else{ %>
								<option value="${fill_machine.id}">${fill_machine.name}</option>
								<% } %>
							</c:forEach>
							<% } %>
						</select>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="control-label col-sm-4">机器名称</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="name" name="name" placeholder="机器名称" value="${machineAdd.name}" required maxlength="20">
				</div>
			</div>
			<div class="form-group">
				<label for="description" class="control-label col-sm-4">机器描述</label>
				<div class="col-sm-6">
					<textarea class="form-control" rows="5" id="description" name="description" placeholder="机器描述">${machineAdd.description}</textarea>
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
<script>
if ($("#machine_type").val() == '<%=Constants.DECOCTION_MACHINE %>') {
    $("#pour_machine").show();
} else {
    $("#pour_machine").hide();
}

$("#machine_type").change(function(){
	if ($("#machine_type").val() == '<%=Constants.DECOCTION_MACHINE %>') {
        $("#pour_machine").show();
    } else {
    	$("#pour_machine").hide();
    }
}); 
</script>
<%@ include file="../foot.jsp"%>