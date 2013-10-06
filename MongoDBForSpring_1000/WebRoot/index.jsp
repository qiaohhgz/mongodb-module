<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<style type="text/css">
div {
	border: solid 1px green;
	height: 25px;
}
</style>
</head>

<body>
	<a href="home.do?page=${page - 1}&limit=10">Back</a>&nbsp;&nbsp;
	<a href="home.do?page=${page + 1}&limit=10">Next</a>
	<br />
	<a href="add.jsp">add</a>
	<c:forEach var="p" items="${ps }">
		<div>
			<f:formatDate value="${p.createDate }" type="both" dateStyle="default" timeStyle="default" />
			<a href="del.do?id=${p.id }">DeleteThis</a>
		</div>
	</c:forEach>
</body>
</html>
