<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Problem Entry</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css" />
<script src="${pageContext.request.contextPath}/resources/scripts/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/problem.js"></script>
</head>
<body data-context-path="${pageContext.request.contextPath}" data-problem-id="${model.id}" data-csrf-parameter-name="${_csrf.parameterName}" data-csrf-token="${_csrf.token}">
	<div id="content">
		<h1>Problem Entry</h1>
		<form id="problem_form" method="post">
			<ul>
				<li>
					<label for="title">Title:</label>
					<input name="title" value="${model.title}"/>
				</li>
				<li>
					<p class="description">
						<label for="description">Description:</label>
						<textarea name="description">${model.description}</textarea>
					</p>	
				</li>
				<li>
					<p class="score">
						<label for="score">Score:</label>
						<input name="score" value="${model.score}" placeholder="1"/>
					</p>	
				</li>
				<li>
					<h4>Test Cases:</h4>
					<ul id="testcases">
						<c:forEach items="${model.tests}" var="test" varStatus="i">
							<li class="testcase" data-test-id="${test.id}">
								<ul>
									<li>
										<label for="input[${i.index}]">Input:</label>
										<input name="input[${i.index}]" value="${test.input}"/>
									</li>
									<li>
										<label for="output[${i.index}]">Expected:</label>
										<input name="output[${i.index}]" value="${test.expected}"/>
									</li>
									<li>
										<label for="maxTime[${i.index}]">Time Limit:</label>
										<input name="maxTime[${i.index}]" value="${test.maxTime}"/>
									</li>
									<li>
										<label for="public[${i.index}]">Is Public:</label>
										<input type="checkbox" name="public[${i.index}]" <c:if test="${test.isPublic()}">checked</c:if>/>
									</li>
								</ul>
							</li>
						</c:forEach>
					</ul>
					<ul class="newtest">
						<li>
							<ul>
								<li>
									<label for="input">Input:</label>
									<input class="newtest" name="input"/>
								</li>
								<li>
									<label for="output">Expected:</label>
									<input class="newtest" name="output"/>
								</li>
								<li>
									<label for="maxTime">Time Limit:</label>
									<input class="newtest" name="maxTime"/>
								</li>
								<li>
									<label for="public">Is Public:</label>
									<input type="checkbox" class="newtest" name="public"/>
								</li>
							</ul>
							<button type="button">Add Test</button>
						</li>
					</ul>
				</li>
				<li>
					<button type="submit" class="submit">Submit</button>
				</li>
			</ul>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		</form>
	</div>
</body>
</html>