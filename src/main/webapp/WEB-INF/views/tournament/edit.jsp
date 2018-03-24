<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create/Edit a Tournament</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css"/>
<script src="${pageContext.request.contextPath}/resources/scripts/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
$(function(){
    $("#add").click(function(){
        $("#availableparticipants > option:selected").each(function(){
            $(this).remove().appendTo("#participants");
        });
    });

    $("#remove").click(function(){
        $("#participants > option:selected").each(function(){
            $(this).remove().appendTo("#availableparticipants");
        });
    });
});​
</script>
</head>
<body>
	<div id="content">
	<h1>Tournament Details</h1>

	<form:form commandName="model" method="post">
		<c:if test="${not empty model.id}">
			<input type="hidden" name="id" value="${model.id}"/>
		</c:if>
		<ul>
			<li><label for="name">Name:</label>
				<form:input path="name"/>
				<p><form:errors path="name" cssClass="error"/></p>
			</li>
			<li><label for="public">Public:</label>
				<form:checkbox path="public"/>
				<p><form:errors path="public" cssClass="error"/></p>
			</li>
			<li><label for="participants">Participants:</label>
				<div style="display: inline-block">
					<h3>Available</h3>
					<select id="availableparticipants" multiple="multiple" rows=10>
						<c:forEach var="coder" items="${availableCoders}">
							<option value="${coder.id}">${coder.firstName} ${coder.lastName}</option>
						</c:forEach>
					</select>
					<button id="add">+</button>
				</div>
				<div style="display: inline-block">
					<h3>Participating</h3>
					<form:select path="coders" multiple="multiple" rows="10">
						<form:options items="${model.coders}" itemValue="id" itemLabel="name"/>
					</form:select>
					<button id="remove">-</button>
				</div>
			</li>
			<li>
			<c:choose>
				<c:when test="${empty model.id}">
					<label for="numberOfRounds">Number of Rounds:</label>
					<input name="numberOfRounds"/>
				</c:when>
				<c:otherwise>
					<ul>
					<c:forEach var="round" items="${model.rounds}">
						<li>
							${round.name} <a href="${pageContext.request.contextPath}/tournament/${model.id}/round/${round.id}/edit">Edit</a>
							<a href="${pageContext.request.contextPath}/tournament/${model.id}/round/${round.id}/delete">Delete</a>
						</li>
					</c:forEach>
					</ul>
					<a href="${pageContext.request.contextPath}/tournament/${model.id}/round/new">New</a>
				</c:otherwise>
			</c:choose>
			</li>
		</ul>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<button type="submit">Submit</button>
	</form:form>
	</div>
</body>
</html>