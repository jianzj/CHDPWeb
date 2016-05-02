<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<% response.setHeader("AuthError", "Unauthenticated"); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>CHDP</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
<style>
.alert {
    width: 200px;
    margin: 20px auto;
}
</style>
</head>
<body>
    <div class="alert alert-danger" role="alert">无用户信息，请先登录</div>
    <script src="<%=request.getContextPath()%>/js/jquery-1.12.3.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/common.js"></script>
</body>
</html>