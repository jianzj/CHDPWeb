<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chdp.chdpweb.Constants" %>
<%@ page import="com.chdp.chdpweb.bean.Process" %>
<%@ page import="com.chdp.chdpweb.bean.Prescription" %>
<%@ page import="java.util.*" %>
<%@ include file="../head.jsp"%>

<h3 class="sub-header">处方流转详细信息

</h3>

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
			<c:if test="${not empty prs}">
				<tr>
					<td><c:out value="${prs.id}" /></td>
					<td><c:out value="${prs.hospital_name}" /></td>
					<td><c:out value="${prs.outer_id}" /></td>
					<td><c:out value="${prs.patient_name}" /></td>
					<td><c:out value="${prs.packet_num}" /></td>
					<td><c:out value="${prs.price}" /></td>
					<td><c:out value="${prs.create_time}" /></td>
				</tr>			
			</c:if>
		</tbody>
	</table>
</div>

<div class="container">
	<div class="row bs-wizard" style="border-bottom:0;">		
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.RECEIVE)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.RECEIVE); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">接方</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">接方</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">接方</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.CHECK)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.CHECK); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">审方</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">审方</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">审方</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.MIX)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.MIX); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">调配</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">调配</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">调配</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.MIXCHECK)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.MIXCHECK); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">调配审核</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">调配审核</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">调配审核</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.SOAK)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.SOAK); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">浸泡</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">浸泡</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">浸泡</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.DECOCT)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.DECOCT); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">煎煮</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">煎煮</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">煎煮</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.POUR)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.POUR); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">灌装</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">灌装</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">灌装</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.CLEAN)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.CLEAN); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">清场</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">清场</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">清场</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.PACKAGE)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.PACKAGE); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">包装</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">包装</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">包装</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
		<% if (request.getAttribute("processMap") != null && ((HashMap)request.getAttribute("processMap")).containsKey(Constants.SHIP)){ %>
		<%     Process pro = (Process)((HashMap)request.getAttribute("processMap")).get(Constants.SHIP); %>
		<%     if (request.getAttribute("prescription") != null && ((Prescription)request.getAttribute("prescription")).getProcess() > pro.getProcess_type()){ %>
		<div class="col-xs-3 bs-wizard-step complete">
		  <div class="text-center bs-wizard-stepnum">出库</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: <%=pro.getFinish() %><br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		<% if (pro.getError_type() == 0){ %>
		 		是否出错: 否<br/>
		 		原因: 无
		 		<% }else{ %>
		 		是否出错: 是<br/>
		 		原因: <%=pro.getError_msg() %>	 		
		 		<% } %>
		 	</span>
		  </div>
		</div>
		<%     }else{ %>
		<div class="col-xs-3 bs-wizard-step active">
		  <div class="text-center bs-wizard-stepnum">出库</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: <%=pro.getBegin() %><br/>
		 		结束时间: 尚未结束<br/>
		 		处理人: <%=pro.getUser_name() %><br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<%     } %>
		<% }else{ %>
		<div class="col-xs-3 bs-wizard-step disabled">
		  <div class="text-center bs-wizard-stepnum">出库</div>
		  <div class="progress"><div class="progress-bar"></div></div>
		  <a href="#" class="bs-wizard-dot"></a>
		  <div class="bs-wizard-info text-center list-group">
		 	<span class="list-group-item">
		 		开始时间: 尚未开始<br/>
		 		结束时间: 尚未开始<br/>
		 		处理人: 暂无<br/>
		 		是否出错: 暂无<br/>
		 		原因: 暂无
		 	</span>
		  </div>
		</div>
		<% } %>
	</div>
</div>
<%@ include file="../foot.jsp"%>