<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ page import="org.apache.shiro.SecurityUtils,com.chdp.chdpweb.bean.User"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>CHDP</title>

<link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap-select.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap-datepicker.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap-datepicker3.css">

<style>
body {
    padding-top: 50px;
}

.sub-header {
    padding-bottom: 10px;
    border-bottom: 1px solid #eee;
}

.navbar-fixed-top {
    border: 0;
}

.sidebar {
    display: none;
}

@media ( min-width : 768px) {
    .sidebar {
        position: fixed;
        top: 51px;
        bottom: 0;
        left: 0;
        z-index: 1000;
        display: block;
        padding: 20px;
        overflow-x: hidden;
        overflow-y: auto;
        background-color: #f5f5f5;
        border-right: 1px solid #eee;
    }
}

.nav-sidebar {
    margin-right: -21px;
    margin-bottom: 20px;
    margin-left: -20px;
}

.nav-sidebar>li>a {
    padding-right: 20px;
    padding-left: 20px;
}

.nav-sidebar>.active>a, .nav-sidebar>.active>a:hover, .nav-sidebar>.active>a:focus {
    color: #fff;
    background-color: #428bca;
}

.main {
    padding: 20px;
}

@media ( min-width : 768px) {
    .main {
        padding-right: 40px;
        padding-left: 40px;
    }
}

.bs-wizard {margin-top: 40px;}

.bs-wizard {border-bottom: solid 1px #e0e0e0; padding: 0 0 10px 0;}
.bs-wizard > .bs-wizard-step {padding: 0; position: relative; height:250px;}
.bs-wizard > .bs-wizard-step + .bs-wizard-step {}
.bs-wizard > .bs-wizard-step .bs-wizard-stepnum {color: #595959; font-size: 16px; margin-bottom: 5px;}
.bs-wizard > .bs-wizard-step .bs-wizard-info {color: #999; font-size: 14px;}
.bs-wizard > .bs-wizard-step > .bs-wizard-dot {position: absolute; width: 30px; height: 30px; display: block; background: #fbe8aa; top: 45px; left: 50%; margin-top: -15px; margin-left: -15px; border-radius: 50%;} 
.bs-wizard > .bs-wizard-step > .bs-wizard-dot:after {content: ' '; width: 14px; height: 14px; background: #fbbd19; border-radius: 50px; position: absolute; top: 8px; left: 8px; } 
.bs-wizard > .bs-wizard-step > .progress {position: relative; border-radius: 0px; height: 8px; box-shadow: none; margin: 20px 0;}
.bs-wizard > .bs-wizard-step > .progress > .progress-bar {width:0px; box-shadow: none; background: #fbe8aa;}
.bs-wizard > .bs-wizard-step.complete > .progress > .progress-bar {width:100%;}
.bs-wizard > .bs-wizard-step.active > .progress > .progress-bar {width:50%;}
.bs-wizard > .bs-wizard-step:first-child.active > .progress > .progress-bar {width:0%;}
.bs-wizard > .bs-wizard-step:last-child.active > .progress > .progress-bar {width: 100%;}
.bs-wizard > .bs-wizard-step.disabled > .bs-wizard-dot {background-color: #f5f5f5;}
.bs-wizard > .bs-wizard-step.disabled > .bs-wizard-dot:after {opacity: 0;}
.bs-wizard > .bs-wizard-step:first-child  > .progress {left: 50%; width: 50%;}
.bs-wizard > .bs-wizard-step:last-child  > .progress {width: 50%;}
.bs-wizard > .bs-wizard-step.disabled a.bs-wizard-dot{ pointer-events: none; }

#eventForm .form-control-feedback {
    top: 0;
    right: -15px;
}

</style>

<script src="<%=request.getContextPath()%>/js/jquery-1.12.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap-select.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
<script>
$(document).ready(function() {
    $('#hospital-datePicker-start')
        .datepicker({
            format: 'yyyy-mm-dd'
        });
    $('#hospital-datePicker-end')
    .datepicker({
        format: 'yyyy-mm-dd'
    });
});
</script>
    
</head>
<body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                    <span class="icon-bar"></span> <span class="icon-bar"></span> <span
                        class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="<%=request.getContextPath()%>">CHDP</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a>你好，<%=((User) SecurityUtils.getSubject().getSession().getAttribute("user")).getName()%></a></li>
                    <li><a href="<%=request.getContextPath()%>/user/changePassword">密码修改</a></li>
                    <li><a href="<%=request.getContextPath()%>/user/logout">注销登录</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-3 col-md-2 sidebar">
                <ul class="nav nav-sidebar">
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("当前处方列表")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/prescription/currentList">当前处方列表</a></li>
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("历史处方列表")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/prescription/historyList">历史处方列表</a></li>
                </ul>
                <ul class="nav nav-sidebar">
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("接方流程列表")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/process/receiveList">接方流程列表</a></li>
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("包装流程列表")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/process/packageList">包装流程列表</a></li>
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("出库流程列表")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/process/shipList">出库流程列表</a></li>
                </ul>
                <ul class="nav nav-sidebar">
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("医院维度统计")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/prescription/hospitalDimensionList">医院维度统计</a></li>
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("员工维度统计")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/prescription/userDimensionList">员工维度统计</a></li>
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("出货单维度统计")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/prescription/orderDimensionList">出货单维度统计</a></li>
                </ul>
                <ul class="nav nav-sidebar">
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("用户管理")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/user/list">用户管理</a></li>
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("医院管理")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/hospital/list">医院管理</a></li>
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("机器管理")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/machine/list">机器管理</a></li>
                    <li <%if(request.getAttribute("nav") != null && request.getAttribute("nav").equals("中药管理")) out.print("class=\"active\""); %>><a href="<%=request.getContextPath()%>/herb/list">中药管理</a></li>
                </ul>
            </div>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      