<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.bean.User" %>
<%@ include file="../head.jsp"%>
<h3 class="sub-header">新建用户</h3>
<div>
	<div>
		<c:if test="${not empty errorMsg}">
			<div class="alert alert-danger" role="alert">${errorMsg}</div>
		</c:if>
		<c:if test="${not empty successMsg}">
			<div class="alert alert-success" role="alert">${successMsg}</div>
		</c:if>
	</div>
	<form class="form-horizontal" action="<%=request.getContextPath()%>/user/add" method="POST">
		<fieldset>
			<div class="form-group">
				<label for="usercode" class="control-label col-sm-4">工号</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="usercode" name="usercode" placeholder="工号" value="${userAdd.usercode}" required maxlength="6">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="control-label col-sm-4">姓名</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="name" name="name" placeholder="姓名" value="${userAdd.name}" maxlength="20">
				</div>
			</div>
			<div class="form-group">
				<label for="password" class="control-label col-sm-4">密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="password" name="password" placeholder="密码" required>
				</div>
			</div>
			<div class="form-group">
				<label for="rePassword" class="control-label col-sm-4">确认密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="rePassword" name="rePassword" placeholder="确认密码" required>
				</div>
			</div>
			<div class="form-group">
				<label for="authority" class="control-label col-sm-4">权限</label>
				<div class="checkbox col-sm-4 required">
				    <label><input name="authority-1024" type="checkbox" value="1024" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&1024)>0) out.print("checked"); %>>管理员</label>　　
					<label><input name="authority-512" type="checkbox" value="512" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&512)>0) out.print("checked"); %>>接方</label>　　　
					<label><input name="authority-256" type="checkbox" value="256" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&256)>0) out.print("checked"); %>>审方</label>　　　
					<label><input name="authority-128" type="checkbox" value="128" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&128)>0) out.print("checked"); %>>调配</label><br />
					<label><input name="authority-64" type="checkbox" value="64" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&64)>0) out.print("checked"); %>>调配审核</label>　
					<label><input name="authority-32" type="checkbox" value="32" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&32)>0) out.print("checked"); %>>浸泡</label>　　　
					<label><input name="authority-16" type="checkbox" value="16" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&16)>0) out.print("checked"); %>>煎煮</label>　　　
					<label><input name="authority-8" type="checkbox" value="8" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&8)>0) out.print("checked"); %>>灌装</label><br />
					<label><input name="authority-4" type="checkbox" value="4" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&4)>0) out.print("checked"); %>>清场</label>　　　
                    <label><input name="authority-2" type="checkbox" value="2" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&2)>0) out.print("checked"); %>>包装</label>　　　
                    <label><input name="authority-1" type="checkbox" value="1" <%if(request.getAttribute("userAdd")!=null && (((User)request.getAttribute("userAdd")).getAuthority()&1)>0) out.print("checked"); %>>运输</label>
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