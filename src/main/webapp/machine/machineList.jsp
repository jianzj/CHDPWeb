<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">机器列表<a class="btn btn-primary" style="float:right;margin-top:-5px" href="<%=request.getContextPath()%>/machine/add">添加机器</a></h3>
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
				<th>机器类型</th>
				<th>机器名称</th>
				<th>关联灌装机</th>
				<th>机器描述</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="machine" items="${machineList}">
				<tr>
					<td><c:out value="${machine.id}" /></td>
					<c:if test="${machine.type == 1}">
						<td><c:out value="煎煮机" /></td>
					</c:if>
					<c:if test="${machine.type == 2}">
						<td><c:out value="灌装机" /></td>
					</c:if>
					<td><c:out value="${machine.name}" /></td>
					<td>
					<c:if test="${machine.pour_machine_name !=null}">
                        ${machine.pour_machine_name}
                    </c:if>
                    </td>
					<td><c:out value="${machine.description}" /></td>
					<td width="190">
						<div class="btn-group" role="group" aria-label="...">
							<a type="button" class="btn btn-danger" onClick="deleteMachine(${machine.id}, '${machine.name}');">删除机器</a>
							<a type="button" class="btn btn-success" onClick="printMachineLabel(${machine.id}, '${machine.name}');">打印标签</a>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="text-right">
	<c:set var="pageUrl" value="machine/list" />
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
    var deleteMachine = function(id, machine_name){
    	$("#assureMsg").html("确认删除机器<strong>"+machine_name+"</strong>？");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/machine/delete?machineId="+id+"&pageNum=${page.pageNum}");
        $("#assureDlg").modal("show");
    };
    
    var printMachineLabel = function(id, machine_name){
    	$("#assureMsg").html("确认打印机器<strong>"+machine_name+"</strong>的标签？");
        $("#assureBtn").attr('href',"<%=request.getContextPath()%>/machine/printMachineLabel?machineId="+id+"&pageNum=${page.pageNum}");
        $("#assureDlg").modal("show");
    };
</script>
<%@ include file="../foot.jsp"%>