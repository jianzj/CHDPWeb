<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>

<form action="<%=request.getContextPath()%>/prescription/currentList" method="GET">
<h3 class="sub-header">
	当前处方列表
	<span>
		<select class="selectpicker" data-live-search="true" data-width="fit" id="hospital" name="hospital">
			<option value="ALL">全部医院</option>
			<c:forEach var='hosp' items="${hospitalList}">
				<c:if test="${hospital == hosp.name}">
				<option value="${hosp.name}" selected>${hosp.name}</option>
				</c:if>
				<c:if test="${hospital != hosp.name}">
				<option value="${hosp.name}">${hosp.name}</option>
				</c:if>
			</c:forEach>
		</select>
		<select class="selectpicker" data-live-search="true" data-with="fit" id="process" name="process">
			<option value=0>全部流程</option>
			<c:if test="${process == 1}">
			<option value=1 selected>接方</option>
			</c:if>
			<c:if test="${process != 1}">
			<option value=1>接方</option>
			</c:if>
			<c:if test="${process == 2}">
			<option value=2 selected>审方</option>
			</c:if>
			<c:if test="${process != 2}">
			<option value=2>审方</option>
			</c:if>
			<c:if test="${process == 3}">
			<option value=3 selected>调配</option>
			</c:if>
			<c:if test="${process != 3}">
			<option value=3>调配</option>
			</c:if>
			<c:if test="${process == 4}">
			<option value=4 selected>调配审核</option>
			</c:if>
			<c:if test="${process != 4}">
			<option value=4>调配审核</option>
			</c:if>
			<c:if test="${process == 5}">
			<option value=5 selected>浸泡</option>
			</c:if>
			<c:if test="${process != 5}">
			<option value=5>浸泡</option>
			</c:if>
			<c:if test="${process == 6}">
			<option value=6 selected>煎煮</option>
			</c:if>
			<c:if test="${process != 6}">
			<option value=6>煎煮</option>
			</c:if>
			<c:if test="${process == 7}">
			<option value=7 selected>灌装</option>
			</c:if>
			<c:if test="${process != 7}">
			<option value=7>灌装</option>
			</c:if>
			<c:if test="${process == 8}">
			<option value=8 selected>清场</option>
			</c:if>
			<c:if test="${process != 8}">
			<option value=8>清场</option>
			</c:if>
			<c:if test="${process == 9}">
			<option value=9 selected>包装</option>
			</c:if>
			<c:if test="${process != 9}">
			<option value=9>包装</option>
			</c:if>
			<c:if test="${process == 10}">
			<option value=10 selected>出库</option>
			</c:if>
			<c:if test="${process != 10}">
			<option value=10>出库</option>
			</c:if>			
		</select>
		<button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
	</span>
</h3>
</form>
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
					<td><c:out value="${prs.phase_name}" /></td>
					<td width="320">
						<div class="btn-group" role="group" aria-label="...">
							<% if (request.getParameter("pageNum") == null){ %>
							<% 		request.setAttribute("page_num", 1); %>
							<% }else{ %>
							<% 		request.setAttribute("page_num", request.getParameter("pageNum")); %>
							<% } %>
						    <a type="button" class="btn btn-info" href="<%=request.getContextPath()%>/process/showAllProcs?prsId=${prs.id}&from=CURRENT">状态</a>
							<a type="button" class="btn btn-default" href="<%=request.getContextPath()%>/prescription/modify?prsId=${prs.id}&from=CURRENT">修改</a>
							<a type="button" class="btn btn-primary" onClick="printSinglePrs(${prs.id},'${hospital}',${process},${page_num},'${prs.hospital_name}','${prs.outer_id}')">打印标签</a>
							<a type="button" class="btn btn-success" onClick="printSinglePackage(${prs.id},'${hospital}',${process},${page_num},'${prs.hospital_name}','${prs.outer_id}')">打印包装标签</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<% request.setAttribute("hospital", (String)request.getAttribute("hospital")); %>
	<% request.setAttribute("process", (Integer)request.getAttribute("process")); %>
	<c:set var="pageUrl" value="prescription/currentList" />
	<%@ include file="../common/nav.jsp"%>
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
	var printSinglePackage = function(id, selectedHospital, process, pageNum, hospital_name, outer_id){
		$("#assureMsg").html("打印处方包装标签 <strong>"+hospital_name+":"+outer_id+"</strong> ？");
	    $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/printSingleLabel?printType=PACKAGE&from=CURRENT&prsId="+id+"&process="+process+"&hospital="+selectedHospital+"&pageNum="+pageNum);
	    $("#assureDlg").modal("show");
	};

	var printSinglePrs = function(id, selectedHospital, process, pageNum, hospital_name, outer_id){
		$("#assureMsg").html("打印处方标签 <strong>"+hospital_name+":"+outer_id+"</strong> ？");
	    $("#assureBtn").attr('href',"<%=request.getContextPath()%>/prescription/printSingleLabel?printType=PRS&from=CURRENT&prsId="+id+"&process="+process+"&hospital="+selectedHospital+"&pageNum="+pageNum);
	    $("#assureDlg").modal("show");
	};
</script>
<%@ include file="../foot.jsp"%>