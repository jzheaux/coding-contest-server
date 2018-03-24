<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css" />
<title>Login</title>
</head>
<body>
	<div id="content">
		<h1>Neumont Coding Contest</h1>
		<form id="submit" method="post">
			<c:if test="${not empty param.error}">
				<div class="alert alert-error">    
                    Invalid username and password.
            	</div>
            </c:if>
			<ul>
				<li>
					<label for="username">Username:</label>
					<input name="username"/>		
				</li>
				<li>
					<label for="password">Password:</label>
					<input name="password" type="password"/>
				</li>
			</ul>
			 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<button type="submit">Login</button>
			<a href="${pageContext.request.contextPath}/register">Register</a>
		</form>
	</div>
</body>
</html>