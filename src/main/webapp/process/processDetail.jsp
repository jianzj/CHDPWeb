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

<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th>处方类型</th>
                <th>先煎</th>
                <th>后下</th>
                <th>包煎</th>
                <th>冲服</th>
                <th>烊化</th>
                <th>另煎</th>
            </tr>
        </thead>
        <tbody>
            <c:if test="${not empty currentPrs}">
                <tr>
                    <td>
                    <c:choose>
					    <c:when test="${currentPrs.class_of_medicines == 1}">
                                                                解表或芳香类药
					    </c:when>
					    <c:when test="${currentPrs.class_of_medicines == 2}">
                                                                一般治疗药
					    </c:when>
					    <c:when test="${currentPrs.class_of_medicines == 3}">
                                                                调理滋补药
                        </c:when>
					    <c:otherwise>
                        &nbsp;
					    </c:otherwise>
					</c:choose>
                    </td>
                    <td><c:out value="${currentPrs.decoct_first_list}" /></td>
                    <td><c:out value="${currentPrs.decoct_later_list}" /></td>
                    <td><c:out value="${currentPrs.wrapped_decoct_list}" /></td>
                    <td><c:out value="${currentPrs.take_drenched_list}" /></td>
                    <td><c:out value="${currentPrs.melt_list}" /></td>
                    <td><c:out value="${currentPrs.decoct_alone_list}" /></td>
                </tr>           
            </c:if>
        </tbody>
    </table>
</div>

<div class="row bs-wizard" style="border-bottom:0;">
<% if (request.getAttribute("nodeList") != null){ %>
    <% int step = 0; %>
	<% for (Node node : (List<Node>)request.getAttribute("nodeList")){ %>
		<% if (node.getStatus() == 0){ %>
		<div class="col-xs-3 bs-wizard-step disabled"<% if(step%4==0){ %> style="clear:both"<%} %>>
		  <div class="text-center bs-wizard-stepnum"><strong><%=node.getNodeTypeName() %></strong></div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
            <div class="bs-wizard-info text-center list-group">
                <div class="bs-callout bs-callout-default">
					<h4 class="txt-center">
						<span class="glyphicon glyphicon-flag"></span> 尚未开始
					</h4>
                </div>
            </div>
		</div>	
		<% } else if (node.getStatus() == 1){ %>
		<div class="col-xs-3 bs-wizard-step disabled"<% if(step%4==0){ %> style="clear:both"<%} %>>
		  <div class="text-center bs-wizard-stepnum"><strong><%=node.getNodeTypeName() %></strong></div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<div class="txt-center">
		 	    <div class="bs-callout bs-callout-info">
                <% if (node.getNodeType() == Constants.SHIP){ %>
			 		<h4><span class="glyphicon glyphicon-flag"></span> <%=node.getOrderStatus() %></h4>
			 		<% if (node.getResolvedBy() != null){ %>
			 		<p><span class="glyphicon glyphicon-print"></span> 打印: <%=node.getStartTime() %></p>
			 		<p><span class="glyphicon glyphicon-user"></span> 处理人: <%=node.getResolvedBy() %></p>
			 		<% } %>
		 		<% }else if (node.getSpecialDisplay()) { %>
		 		    <h4><span class="glyphicon glyphicon-flag"></span> 正在处理</h4>
			 		<p><span class="glyphicon glyphicon-play-circle"></span> 开始: <%=node.getStartTime() %></p>
			 		<p><span class="glyphicon glyphicon-user"></span> 处理人: <%=node.getResolvedBy() %></p>
		 		<% }else{ %>
                    <h4><span class="glyphicon glyphicon-flag"></span> 等待处理</h4>
		 		<% } %>
		 		</div>
		 	</div>
		  </div>
		</div>				
		<% } else{ %>
		<div class="col-xs-3 bs-wizard-step complete"<% if(step%4==0){ %> style="clear:both"<%} %>>
		  <div class="text-center bs-wizard-stepnum"><strong><%=node.getNodeTypeName() %></strong></div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<div class="txt-center">
		 	    <% if (node.getErrorStatus() != 0){ %>
                    <div class="bs-callout bs-callout-danger">
                    <h4><span class="glyphicon glyphicon-repeat"></span> 出错: <%=Constants.getErrorName(node.getErrorStatus()) %></h4>
                    <p><span class="glyphicon glyphicon-time"></span> 时间: <%=node.getEndTime() %></p>
                    <p><span class="glyphicon glyphicon-user"></span> 处理人: <%=node.getResolvedBy() %></p>
                    <p><span class="glyphicon glyphicon-warning-sign"></span> 原因: <%=node.getErrorMsg() %></p>
                    </div>
                <% } else { %>
                    <div class="bs-callout bs-callout-success">
		 		<% if (node.getNodeType() == Constants.SHIP){ %>
			 	    <h4><span class="glyphicon glyphicon-flag"></span> <%=node.getOrderStatus() %></h4>
			 	    <p><span class="glyphicon glyphicon-print"></span> 打印: <%=node.getStartTime() %></p>
			 	    <p><span class="glyphicon glyphicon-time"></span> 出库: <%=node.getEndTime() %></p>
		 		<% }else if (node.getSpecialDisplay()){ %>
		 		    <h4><span class="glyphicon glyphicon-flag"></span> 处理完成</h4>
			 		<% if (node.getNodeType() == Constants.DECOCT){ %>
				 		<p><span class="glyphicon glyphicon-time"></span> 煎煮: <%=node.getDecoctTime() %></p>
                        <p><span class="glyphicon glyphicon-time"></span> 保温: <%=node.getHeatTime() %></p>
                        <p><span class="glyphicon glyphicon-scale"></span> 煎煮机: <%=node.getMachineName() %></p>
			 		<% }else if (node.getNodeType() == Constants.POUR){ %>
			 		    <p><span class="glyphicon glyphicon-play-circle"></span> 开始: <%=node.getStartTime() %></p>
                        <p><span class="glyphicon glyphicon-off"></span> 结束: <%=node.getEndTime() %></p>
                        <p><span class="glyphicon glyphicon-scale"></span> 灌装机: <%=node.getMachineName() %></p>
				 	<% }else{ %>
				 	    <p><span class="glyphicon glyphicon-play-circle"></span> 开始: <%=node.getStartTime() %></p>
                        <p><span class="glyphicon glyphicon-off"></span> 结束: <%=node.getEndTime() %></p>
				 	<% } %>
		 		<% }else { %>
		 		    <h4><span class="glyphicon glyphicon-flag"></span> 处理完成</h4>
			 		<p><span class="glyphicon glyphicon-time"></span> 时间: <%=node.getEndTime() %></p>
			 	<% } %>
			 		<p><span class="glyphicon glyphicon-user"></span> 处理人: <%=node.getResolvedBy() %></p>
			        </div>
			    <% } %>
		 	</div>
		  </div>
		</div>				
		<% } %>
		<% step++; %>
	<% } %>
<% } %>
</div>
<%@ include file="../foot.jsp"%>