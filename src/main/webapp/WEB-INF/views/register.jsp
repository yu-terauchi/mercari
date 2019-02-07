<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<!-- css -->
<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet"
	integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN"
	crossorigin="anonymous" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
	integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
	crossorigin="anonymous" />
<link rel="stylesheet" href="./mercari.css" />
<!-- script -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
	integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
	crossorigin="anonymous"></script>
<title>Rakus Items</title>
</head>
<body>
	<!-- navbar -->
	<nav class="navbar navbar-inverse">
		<a class="navbar-brand" href="${pageContext.request.contextPath}/showItemsList/toItems?currentPageId=1">Rakus Items</a>
	</nav>

	<!-- register form -->
	<div id="login" class="container">
		<div class="panel panel-default">
			<div class="panel-heading">Register Account</div>
			<div class="panel-body">
				<form:form modelAttribute="usersForm" action="${pageContext.request.contextPath}/register/registerUser" method="POST">
					<div class="form-group">
						<label for="name">user name</label> 
						<input type="text" name="name" class="form-control" id="name">
						<form:errors path="name" cssStyle="color:red" element="div" />
					</div>
					<div class="form-group">
						<label for="mail">Email address</label> 
						<input type="email" name="email" class="form-control" id="mail">
						<form:errors path="email" cssStyle="color:red" element="div" />
					</div>
					<div class="form-group">
						<label for="password">password</label>
						<input type="text" name="password" class="form-control" id="password">
						<form:errors path="password" cssStyle="color:red" element="div" />
					</div>
					<div class="form-group">
						<label for="confirmPassword">confirm password</label>
						<input type="text" name="confirmPassword" class="form-control" id="confirmPassword">
						<form:errors path="confirmPassword" cssStyle="color:red" element="div" />
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form:form>
			</div>
		</div>
		<div>
			<a type="button" class="btn btn-default" href="${pageContext.request.contextPath}/register/toLogin"><i
				class="fa fa-reply"></i>&nbsp;Login page</a>
		</div>
</body>
</html>