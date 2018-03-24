<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css" />
<title>${model.name}</title>
</head>
<body>
	<div id="content">
		<h1>Round Details</h1>
		<form:form commandName="model" method="post">
			<c:if test="${not empty model.id}">
				<input type="hidden" name="id" value="${model.id}"/>
			</c:if>
			<ul>
				<li><label for="name">Name:</label>
					<form:input path="name"/>
					<p><form:errors path="name" cssClass="error"/></p>
				</li>
				<li><label for="maxTime">Max Time:</label>
					<form:input path="maxTime"/>
					<p><form:errors path="maxTime" cssClass="error"/></p>
				</li>
				<li><label for="startDate">Start Time:</label>
					<form:input type="datetime" path="startDate"/>
					<p><form:errors path="startDate" cssClass="error"/></p>
				</li>
				<li><label for="endDate">End Time:</label>
					<form:input type="datetime" path="endDate"/>
					<p><form:errors path="endDate" cssClass="error"/></p>
			</ul>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<button type="submit">Submit</button>
		</form:form>
		<div class="onetomany">
			<h4>Problems:</h4>
			<ul>
			<c:forEach var="problem" items="${model.problems}">
				<li>
					${problem.title} <a href="${pageContext.request.contextPath}/problem/${problem.id}/edit">Edit</a>
					<form class="delete" action="${pageContext.request.contextPath}/tournament/${model.tournament.id}/round/${model.id}/problem/${problem.id}/delete" method="post">
						<a href="#" onclick="this.parentElement.submit(); return false;">Delete</a>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					</form>
				</li>
			</c:forEach>
			</ul>
			<a href="${pageContext.request.contextPath}/tournament/${model.tournament.id}/round/${model.id}/problem/new">New</a>
		</div>
	</div>
</body>
</html>