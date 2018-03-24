<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css"/>
<script src="${pageContext.request.contextPath}/resources/scripts/jquery-1.10.2.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/countdown.js"></script>
<title>${model.round.name} Report</title>
</head>
<body>
	<div id="content">
		<h1>Submissions By Coder</h1>
		<c:choose>
			<c:when test="${model.round.secondsUntilEndTime >= 0}">
				<p>Time Remaining: <span class="countdown" data-seconds="${model.round.secondsUntilEndTime}"/></p> 
			</c:when>
			<c:otherwise>
				<p>This round is over; the results are below.</p>
			</c:otherwise>
		</c:choose>
		<table>
			<thead>
				<tr>
					<th>Coder</th>
					<th># of Correct Submissions</th>
					<th>Time of Last Submission</th>
					<th>Score</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entry" items="${model.coders}">
					<tr>
						<td><a href="${pageContext.request.contextPath}/coder/${entry.key.id}">${entry.key.username}</a></td>
						<td>${fn:length(entry.value)}</td>
						<td>
							<c:forEach var="submission" items="${entry.value}" varStatus="i">
								<c:if test="${i.index eq 0}">
									${submission.date}
								</c:if>
							</c:forEach>
						</td>
						<td>
							<c:set var="total" value="0"/>
							<c:forEach var="submission" items="${entry.value}">
								<c:set var="total" value="${total + submission.problem.score}"/>
							</c:forEach>
							${total}
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<p>&nbsp;</p>
	</div>
</body>
</html>