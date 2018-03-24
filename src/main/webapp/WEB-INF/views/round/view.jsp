<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css"/>
<script src="${pageContext.request.contextPath}/resources/scripts/jquery-1.10.2.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/countdown.js"></script>
<title>${model.name}</title>
</head>
<body>
	<div id="content">
	<section>
		<p class="time">Time Remaining: <span class="countdown" data-seconds="${model.secondsUntilEndTime}"></span></p>
		<p class="time">Score: <span class="score">${model.score}</span></p>
		<h1>Problem Set</h1>
		<p>Select any problem to begin. Here, your number of submissions for each problem is presented as well as the time of your most recent submission. Click "View" to view the problem and supply a solution.</p>

		<table>
			<thead>
				<tr>
					<th>Title</th>
					<th>Score</th>
					<th>Number of Submissions</th>
					<th>Last Submission Time</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="problem" items="${model.problems}">
					<tr <c:if test="${problem.passed}">class="passed"</c:if>>
						<td>${problem.title}</td>
						<td>${problem.score}</td>
						<td>${problem.numberOfSubmissions}</td>
						<td>${problem.lastSubmissionTime}</td>
						<td><a class="small button" href="${model.id}/problem/${problem.id}">View</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<p>&nbsp;</p>
	</section>
	</div>
</body>
</html>