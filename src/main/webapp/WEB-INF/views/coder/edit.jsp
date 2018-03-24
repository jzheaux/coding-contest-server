<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/styles/contest.css" />
<title>Apply for Neumont Coding Contest</title>
</head>
<body>
	<div id="content">
		<h1>Neumont Coding Contest Application</h1>
		<form:form commandName="model" method="post">
			<ul>
				<li><label for="firstName">First Name:</label>
					<form:input path="firstName"/>
					<p><form:errors path="firstName" cssClass="error"/></p>
				</li>
				<li><label for="lastName">Last Name:</label>
					<form:input path="lastName"/>
					<p><form:errors path="lastName" cssClass="error"/></p>
				</li>
				<li><label for="email">Email:</label> 
					<form:input path="email"/>
					<p><form:errors path="email" cssClass="error"/></p>
				</li>
				<li><label for="username">User Name:</label> <form:input
					path="username"/>
					<p><form:errors path="username" cssClass="error"/></p></li>
				<li><label for="password">Password:</label> <input
					name="password" type="password" value="${model.passwordAsString}" />
					<p><form:errors path="password" cssClass="error"/></p>
					</li>
				<li><label for="confirmPassword">Confirm:</label> <input
					name="confirmPassword" type="password" value="${model.confirmPasswordAsString}" />
					<p><form:errors path="confirmPassword" cssClass="error"/></p>
				</li>
				<li><label for="preferredLanguage">Language:</label> <select
					name="preferredLanguage">
						<option value="JAVA" <c:if test="${model.preferredLanguage == 'JAVA'}">selected</c:if>>Java</option>
						<option value="CSHARP" <c:if test="${model.preferredLanguage == 'CSHARP'}">selected</c:if>>C#</option>
					</select>
					<p></p>
				</li>
				<li><label for="age">Age:</label> <form:input
					path="age"/>
					<p><form:errors path="age" cssClass="error"/></p>
				</li>
				<li><label for="graduationYear">Year:</label> <form:input
					path="graduationYear"/>
					<p><form:errors path="graduationYear" cssClass="error"/></p>
				</li>
				<li><label for="imageLocation">Image Url:</label> <input
					name="imageLocation" value="${model.imageLocation}"/></li>
					<p></p>
				<li>
					<p>
						<label for="bio">Bio:</label>
						<form:textarea path="bio"/>
						<p><form:errors path="bio" cssClass="error"/></p>
					</p>
				</li>
				<li>
					<!-- Captcha -->
				</li>
			</ul>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<button type="submit">Apply</button>
		</form:form>
	</div>
</body>
</html>