<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Profile for ${model.firstName} ${model.lastName}</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/styles/contest.css" />
</head>
<body>
	<div id="content">
		<h1>${model.firstName} ${model.lastName}</h1>
		<section class="bio">
			<c:choose>
				<c:when test="${not empty model.imageLocation}">
					<img src="${model.imageLocation}"/>	
				</c:when>
				<c:otherwise>
					<img src="${pageContext.request.contextPath}/resources/images/${model.id}/user.jpg"/>
				</c:otherwise>
			</c:choose>
			<ul>
				<li>
					<label>Age:</label>
					<span>${model.age}</span>
				</li>
				<li>
					<label>Year:</label>
					<span>${model.graduationYear}</span>
				</li>
			</ul>
			<article>
				${model.bio}
			</article>
		</section>
	</div>
</body>
</html>