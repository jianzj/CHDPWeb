<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ page import="com.chdp.chdpweb.bean.Node" %>
<%@ page import="com.chdp.chdpweb.bean.Process" %>
<%@ page import="com.chdp.chdpweb.bean.Prescription" %>
<%@ page import="java.util.*" %>
<%@ include file="../head.jsp"%>

<h3 class="sub-header">处方流转详细信息</h3>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>编号</th>
				<th>医院</th>
				<th>医院编号</th>
				<th>姓名</th>
				<th>贴数</th>
				<th>金额</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${not empty currentPrs}">
				<tr>
					<td><c:out value="${currentPrs.uuid}" /></td>
					<td><c:out value="${currentPrs.hospital_name}" /></td>
					<td><c:out value="${currentPrs.outer_id}" /></td>
					<td><c:out value="${currentPrs.patient_name}" /></td>
					<td><c:out value="${currentPrs.packet_num}" /></td>
					<td><c:out value="${currentPrs.price}" /></td>
					<td><c:out value="${currentPrs.create_time}" /></td>
				</tr>			
			</c:if>
		</tbody>
	</table>
</div>

<div class="row bs-wizard" style="border-bottom:0;">
<% if (request.getAttribute("nodeList") != null){ %>
	<% for (Node node : (List<Node>)request.getAttribute("nodeList")){ %>
		<% if (node.getStatus() == 0){ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum"><%=node.getNodeTypeName() %></div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="txt-center">
		 		<span class="glyphicon glyphicon-flag"></span> 尚未开始
		 	</span>
		  </div>
		</div>	
		<% } else if (node.getStatus() == 1){ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum"><%=node.getNodeTypeName() %></div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="txt-center">
		 		<% if (node.getNodeType() == Constants.SHIP){ %>
			 		<span class="glyphicon glyphicon-flag"></span> <%=node.getOrderStatus() %><br/>
			 		<% if (node.getResolvedBy() != null){ %>
			 		<span class="glyphicon glyphicon-print"></span> 打印时间: <%=node.getStartTime() %><br/>
			 		<span class="glyphicon glyphicon-user"></span> 处理人: <%=node.getResolvedBy() %><br/>
			 		<% } %>
		 		<% }else if (node.getSpecialDisplay()) { %>
		 		    <span class="glyphicon glyphicon-flag"></span> 正在处理<br/>
			 		<span class="glyphicon glyphicon-play-circle"></span> 开始时间: <%=node.getStartTime() %><br/>
			 		<span class="glyphicon glyphicon-user"></span> 处理人: <%=node.getResolvedBy() %><br/>
		 		<% }else{ %>
                    <span class="glyphicon glyphicon-flag"></span> 等待处理<br/>
		 		<% } %>
			 	<% if (node.getErrorStatus() != 0){ %>
				 	<div class="bs-callout bs-callout-danger">
				 	<span class="glyphicon glyphicon-repeat"></span> 流程回退: <%=Constants.getErrorName(node.getErrorStatus()) %><br/>
				 	<span class="glyphicon glyphicon-warning-sign"></span> 回退原因: <%=node.getErrorMsg() %>
				 	</div>
				<% } %>
		 	</span>
		  </div>
		</div>				
		<% } else{ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum"><%=node.getNodeTypeName() %></div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="txt-center">
		 		<% if (node.getNodeType() == Constants.SHIP){ %>
			 		<span class="glyphicon glyphicon-flag"></span> <%=node.getOrderStatus() %><br/>
			 		<span class="glyphicon glyphicon-print"></span> 打印时间: <%=node.getStartTime() %><br/>
			 		<span class="glyphicon glyphicon-time"></span> 出库时间: <%=node.getEndTime() %><br/>
		 		<% }else if (node.getSpecialDisplay()){ %>
		 		    <span class="glyphicon glyphicon-flag"></span> 处理完成<br/>
			 		<span class="glyphicon glyphicon-play-circle"></span> 开始时间: <%=node.getStartTime() %><br/>
			 		<span class="glyphicon glyphicon-off"></span> 结束时间: <%=node.getEndTime() %><br/>
			 		<% if (node.getNodeType() == Constants.DECOCT){ %>
				 		<span class="glyphicon glyphicon-scale"></span> 机器名称: <%=node.getMachineName() %><br/>
				 		<span class="glyphicon glyphicon-time"></span> 煎煮时间: <%=node.getDecoctTime() %><br/>
                        <span class="glyphicon glyphicon-time"></span> 保温时间: <%=node.getHeatTime() %><br/>
			 		<% }else if (node.getNodeType() == Constants.POUR){ %>
                        <span class="glyphicon glyphicon-scale"></span> 机器名称: <%=node.getMachineName() %><br/>	
				 	<% } %>
		 		<% }else { %>
		 		    <span class="glyphicon glyphicon-flag"></span> 处理完成<br/>
			 		<span class="glyphicon glyphicon-time"></span> 处理时间: <%=node.getEndTime() %><br/>
			 	<% } %>
			 		<span class="glyphicon glyphicon-user"></span> 处理人: <%=node.getResolvedBy() %><br/>
				 <% if (node.getErrorStatus() != 0){ %>
				    <div class="bs-callout bs-callout-danger">
				 	<span class="glyphicon glyphicon-repeat"></span> 流程回退: <%=Constants.getErrorName(node.getErrorStatus()) %><br/>
				 	<span class="glyphicon glyphicon-warning-sign"></span> 回退原因: <%=node.getErrorMsg() %>
				 	</div>
			 	<% } %>
		 	</span>
		  </div>
		</div>				
		<% } %>
	<% } %>
<% } %>
</div>
<%@ include file="../foot.jsp"%>