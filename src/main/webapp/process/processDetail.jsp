<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ page import="com.chdp.chdpweb.bean.Process" %>
<%@ page import="com.chdp.chdpweb.bean.Prescription" %>
<%@ page import="java.util.*" %>
<%@ include file="../head.jsp"%>

<h3 class="sub-header">处方流转详细信息</h3>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>ID</th>
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
					<td><c:out value="${currentPrs.id}" /></td>
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

<div class="container">
	<div class="row bs-wizard" style="border-bottom:0;">
		<!-- 已完成的Process -->
		<% if (request.getAttribute("processList") != null && request.getAttribute("currentPrs") != null){ %>
		<%     Iterator<Process> itr = ((List<Process>)request.getAttribute("processList")).iterator(); %>
		<%     Prescription currentPrs = (Prescription)request.getAttribute("currentPrs"); %>
		<%     Process proc = null; %>
		<%     while (itr.hasNext()){ %>
		<%         proc = itr.next(); %>
			<%     if (proc.getFinish()!=null){ %>
			<div class="col-xs-3 bs-wizard-step complete">
			<%     } else if (proc.getBegin()==null){ %>
			<div class="col-xs-3 bs-wizard-step active">
			<%     } else { %>
			<div class="col-xs-3 bs-wizard-step disabled">
			<%     } %>
			<div class="text-center bs-wizard-stepnum"><%=Constants.getProcessName(proc.getProcess_type()) %></div>
		    <div class="progress"><div class="progress-bar"></div></div>
		    <a href="#" class="bs-wizard-dot"></a>
		    <div class="bs-wizard-info text-center list-group">
		 	<span class="txt-center">
		 		<% if (proc.getProcess_type() != Constants.DECOCT && proc.getProcess_type() != Constants.SOAK && proc.getProcess_type() != Constants.CLEAN){ %>
		 		<c:if test="${proc.finish == null}">等待处理<br /></c:if>
		 		<c:if test="${proc.finish != null}">处理时间: <%=proc.getFinish() %><br/></c:if>
		 		<% } else if (proc.getProcess_type() ==Constants.DECOCT && proc.getBegin()!=null && proc.getFinish()!=null) { %>
                                           开始时间:<%=proc.getBegin() %><br/>
                                           结束时间:<%=proc.getFinish() %><br/>
                <% } else if (proc.getProcess_type() ==Constants.DECOCT && proc.getBegin()!=null && proc.getFinish()!=null) { %>
		 		开始时间:<%=proc.getBegin() %><br/>
                                           结束时间:<%=proc.getFinish() %><br/>
                                           煎煮时间:<%=Constants.getDecoctTime(proc.getBegin(), proc.getFinish(), 1) %><br/>
		 		保温时间:<%=Constants.getHeatTime(proc.getFinish(), 1) %><br/>
		 		<% } else { %>
		 		开始时间:<%=proc.getBegin() %><br/>
                                           结束时间:<%=proc.getFinish() %><br/>
                <% } %>
		 		<%if(proc.getFinish()!=null || (proc.getBegin()!=null && (proc.getProcess_type() == Constants.DECOCT || proc.getProcess_type() == Constants.SOAK || proc.getProcess_type() == Constants.CLEAN))){ %>
		 		处理人: <%=proc.getUser_name() %><br/>
                <% }%>
		 		<% if (proc.getError_type() == 0){ %>
		 		正常
		 		<% } else { %>
		 		出错: <%=Constants.getErrorName(proc.getError_type()) %><br/>
		 		出错原因: <%=proc.getError_msg() %>
		 		<% } %>
		 	</span>
		    </div>
		    </div>
		<%     } %>
		<% } %>
		</div>	
	</div>
	
	<% if (request.getAttribute("currentPrs") != null) { %>
	<div class="row bs-wizard" style="border-bottom:0;">
	<% Prescription currentPrs = (Prescription)request.getAttribute("currentPrs"); %>
	<% int process_type = currentPrs.getProcess() + 1; %>
	<% while (process_type < 11){ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum"><%=Constants.getProcessName(process_type) %></div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		尚未开始！
		 	</span>
		  </div>
		</div>	
	<%     process_type += 1; %>
	<% } %>
	</div>
	<% } %>
</div>
<%@ include file="../foot.jsp"%>