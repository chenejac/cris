<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fmt:setLocale value="sr" />
<fmt:setBundle basename="messages.messages-errors" />

<html>
	<head>
	<title><fmt:message key="errors.error.subject" /></title>
		<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<meta HTTP-EQUIV="Expires" CONTENT="-1">
	<link href="./stylesheet/theme.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<h1 class="error"><fmt:message key="errors.error.subject" /></h1>
		<p class="error"><fmt:message key="errors.error.text" /></p>
		<h2 class="error"><fmt:message key="errors.error.error" />:</h2>
		<form action="mailto:chenejac@uns.ac.rs" method="get">
			<textarea class="error" rows="50" cols="140" name="body">${pageContext.errorData.throwable}:
				<c:forEach var="stackTraceElement"
			items="${pageContext.errorData.throwable.stackTrace}">
				${stackTraceElement}
				</c:forEach> 		
			</textarea>
			<input type="submit" value="Posalji" />
		</form>
	</body>
</html>
