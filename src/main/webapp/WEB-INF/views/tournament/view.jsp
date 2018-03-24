<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Available Tournaments</title>
</head>
<body>
	<div id="content">
		<h1>Neumont Coder Challenges</h1>
		<p>Looks like you are enrolled in more than one challenge. (Yay!) Make your selection below.</p>
		<table id="tournaments">
			<thead>
				<tr>
					<th>Name</th>
					<th>Current Round</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th></th>
				</tr>
			</thead>
			<c:forEach var="tournament" items="${model}">
				<tbody>
					<tr>
						<td>
							${tournament.name}
						</td>
						<td>
							${tournament.nextRound}
						</td>
						<td>
							${tournament.startDate}
						</td>
						<td>
							${tournament.endDate}
						</td>
						<td>
							<a class="small button" href="/tournament/${tournament.id}/round/${tournament.nextRound}/notice">Begin</a>
						</td>
					</tr>
				</tbody>
			</c:forEach>
		</table>
		<p>&nbsp;</p>
	</div>
</body>
</html>