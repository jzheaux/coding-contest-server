<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/resources/styles/contest.css" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/resources/scripts/jquery-1.10.2.min.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/resources/scripts/countdown.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>${model.name}</title>
	</head>
	<body>
		<div id="content">
			<h1>${model.name} will begin in:</h1>
			<c:choose>
				<c:when test="${model.secondsUntilStartTime >= 0}">
					<div data-seconds="${model.secondsUntilStartTime}" class="countdown"></div>
				</c:when>
				<c:otherwise>
					<p>This round has already completed.</p>
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>