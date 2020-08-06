<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<fmt:setLocale value="sr" />
<fmt:setBundle basename="messages.messages-errors" />

<html>
	<head>
		<title><fmt:message key="errors.notFound.subject" /></title>
		<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<meta HTTP-EQUIV="Expires" CONTENT="-1">
		<link href="./stylesheet/theme.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<h1 class="notFound"><fmt:message key="errors.notFound.subject" /></h1>
		<p class="notFound"><fmt:message key="errors.notFound.text" /></p>
	</body>
</html>
