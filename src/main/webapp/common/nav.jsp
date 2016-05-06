<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.github.pagehelper.PageInfo"%>
<nav>
	<ul class="pagination">
		<li <c:if test="${!page.hasPreviousPage}"> class="disabled"</c:if>><a
			href="<c:if test="${page.hasPreviousPage}"><%=request.getContextPath()%>/${pageUrl}?startTime=${startTime}&endTime=${endTime}&orderId=${orderId}&userId=${userId}&hospital=${hospital}&process=${process}&pageNum=${page.pageNum-1}</c:if>"
			aria-label="上一页"> <span aria-hidden="true">&laquo;</span>
		</a></li>
		<%
			PageInfo pageInfo = (PageInfo) request.getAttribute("page");
			int start = pageInfo.getPageNum() - 2 >= 1 ? pageInfo.getPageNum() - 2 : 1;
			int end = start + 4 > pageInfo.getPages() ? pageInfo.getPages() : start + 4;
			if (end - start < 4)
				start = end - 4 >= 1 ? end - 4 : 1;
			request.setAttribute("start", start);
			request.setAttribute("end", end);
		%>
		<c:forEach var="p" begin="${start}" end="${end}">
			<li <c:if test="${page.pageNum==p}"> class="active"</c:if>><a
				href="<%=request.getContextPath()%>/${pageUrl}?startTime=${startTime}&endTime=${endTime}&orderId=${orderId}&userId=${userId}&hospital=${hospital}&process=${process}&pageNum=${p}">${p}</a></li>
		</c:forEach>
		<li <c:if test="${!page.hasNextPage}"> class="disabled"</c:if>><a
			href="<c:if test="${page.hasNextPage}"><%=request.getContextPath()%>/${pageUrl}?startTime=${startTime}&endTime=${endTime}&order=${orderId}&userId=${userId}&hospital=${hospital}&process=${process}&pageNum=${page.pageNum+1}</c:if>"
			aria-label="下一页"> <span aria-hidden="true">&raquo;</span>
		</a></li>
	</ul>
</nav>