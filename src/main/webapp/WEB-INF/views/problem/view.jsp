<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Problem One - Round One</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/codemirror.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css" />
<script src="${pageContext.request.contextPath}/resources/scripts/jquery-1.10.2.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/codemirror.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/clike.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/matchbrackets.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/submission.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/countdown.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.js"></script>
<link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.css"/>
</head>
<body data-csrf-header="${_csrf.headerName}" data-csrf-token="${_csrf.token}" data-context-path="${pageContext.request.contextPath}"
	data-submission-id="${model.id}" data-problem-id="${model.problem.id}" data-round-id="${model.round.id}" data-tournament-id="${model.round.tournament.id}">
	<div id="content">
		<p class="time">Time Remaining: <span class="countdown" data-seconds="${model.round.secondsUntilEndTime}"></span></p>
		<section class="problem">
			<h1>Problem</h1>
			<p class="description">${model.problem.description}</p>
			<table class="samples">
				<thead>
					<tr>
						<th></th>
						<th>Input</th>
						<th>Expected Output</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="test" items="${model.problem.expectations}">
						<tr>
							<td data-test-id="${test.id}" class="result"></td>
							<td>${test.input}</td>
							<td>${test.expected}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</section>
		<section class="solution">
			<h1>Your Solution</h1>
			<div id="languageselector">
				<button data-language="JAVA" <c:if test="${model.owner.preferredLanguage == 'JAVA'}">class="selected"</c:if>>Java</button>
				<button data-language="CSHARP" <c:if test="${model.owner.preferredLanguage == 'CSHARP'}">class="selected"</c:if>>C#</button>
			</div>
			<textarea id="solution" name="solution">${model.code}</textarea>
			<div id="submit">
				<button>Compile</button>
				<button>Submit</button>
			</div> 
		</section>
	</div>
</body>
</html>