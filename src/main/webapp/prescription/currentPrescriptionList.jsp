<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>

<h3 class="sub-header">当前处方列表
	<div class="btn-group">
	  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	    选择指定医院 <span class="caret"></span>
	  </button>
	  <ul class="dropdown-menu">
	    <c:forEach var='hospital' items="${hospitalList}">
	        <% if (request.getAttribute("process") != null){ %>
	        <li><a href="<%=request.getContextPath() %>/prescription/currentList?hospital=${hospital.name}&process=${process}"><c:out value="${hospital.name}" /></a></li>
	        <% }else{ %>
	        <li><a href="<%=request.getContextPath() %>/prescription/currentList?hospital=${hospital.name}&process="><c:out value="${hospital.name}" /></a></li>	        
	        <% } %>
	    </c:forEach>
	  </ul>
	</div>
    <div class="btn-group">
	  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	    选择指定医院 <span class="caret"></span>
	  </button>
	  <ul class="dropdown-menu">
	    <c:forEach var='hospital' items="${hospitalList}">
	        <li><a href="<%=request.getContextPath() %>/process/packageList?hospital=${hospital.name}"><c:out value="${hospital.name}" /></a></li>
	    </c:forEach>
	  </ul>
	</div>
</h3>
<div>
	<c:if test="${not empty errorMsg}">
		<div class="alert alert-danger" role="alert">${errorMsg}</div>
	</c:if>
	<c:if test="${not empty successMsg}">
		<div class="alert alert-success" role="alert">${successMsg}</div>
	</c:if>
</div>
<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>ID</th>
				<th>医院</th>
				<th>医院编号</th>
				<th>姓名</th>
				<th>贴数</th>
				<th>当前状态</th>
				<th>处理人员</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="prs" items="${currentPrsList}">
				<tr>
					<td><c:out value="${prs.id}" /></td>
					<td><c:out value="${prs.hospital_name}" /></td>
					<td><c:out value="${prs.outer_id}" /></td>
					<td><c:out value="${prs.patient_name}" /></td>
					<td><c:out value="${prs.packet_num}" /></td>
					<td>
					<c:if test="${prs.process == 1}"><c:out value="接方" /></c:if>
					<c:if test="${prs.process == 9}"><c:out value="包装" /></c:if>
					</td>
					<td><c:out value="${prs.user_name}" /></td>
					<td width="300">
						<div class="btn-group" role="group" aria-label="...">
							<a type="button" class="btn btn-default" href="">处方修改</a>
							<a type="button" class="btn btn-primary" href="">打印标签</a>
							<a type="button" class="btn btn-success" onClick="">打印包装标签</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<div class="modal fade" id="assureDlg" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-body" id="assureMsg"></div>
			<div class="modal-footer">
				<a type="button" class="btn btn-success" id="assureBtn">确认</a> <a type="button" class="btn btn-default"
					data-dismiss="modal">取消</a>
			</div>
		</div>
	</div>
</div>
<script>
    var deletePrs = function(id, process, hospital_name, outer_id){
    	$("#assureMsg").html("确认删除处方 <strong>"+hospital_name+":"+outer_id+"</strong>？");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/delete?prsId="+id+"&process="+process);
        $("#assureDlg").modal("show");
    };
</script>
<%@ include file="../foot.jsp"%>