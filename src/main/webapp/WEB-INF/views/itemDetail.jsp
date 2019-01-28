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
<link rel="stylesheet" href="./css/mercari.css" />
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
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${pageContext.request.contextPath}/showItemsList/toItems">Rakus Items</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<div>
				<ul class="nav navbar-nav navbar-right">
					<li><a id="logout" href="./login.html">Logout&nbsp;<i
							class="fa fa-power-off"></i></a></li>
				</ul>
				<p class="navbar-text navbar-right">
					<span id="loginName">user: userName</span>
				</p>
			</div>
		</div>
	</nav>

	<!-- details -->
	<div class="container">
		<a type="button" class="btn btn-default" href="${pageContext.request.contextPath}/showItemsList/backItems"><i
			class="fa fa-reply"></i> back</a>
		<h2>Details</h2>
		<div id="details">
			<table class="table table-hover">
				<tbody>
					<tr>
						<th>ID</th>
						<td><c:out value="${itemDetail.id}"/></td>
					</tr>
					<tr>
						<th>name</th>
						<td><c:out value="${itemDetail.name}"/></td>
					</tr>
					<tr>
						<th>price</th>
						<td>$<c:out value="${itemDetail.price}"/></td>
					</tr>
					<tr>
						<th>category</th>
							<td><c:out value="${grandson.nameAll}"/></td>
					</tr>
					<tr>
						<th>brand</th>
						<td><c:out value="${itemDetail.brand}"/></td>
					</tr>
					<tr>
						<th>condition</th>
						<td><c:out value="${itemDetail.conditionId}"/></td>
					</tr>
					<tr>
						<th>description</th>
						<td><c:out value="${itemDetail.description}"/></td>
					</tr>
				</tbody>
			</table>
			<a type="button" class="btn btn-default" href="./edit.html"><i
				class="fa fa-pencil-square-o"></i>&nbsp;edit</a>
		</div>
	</div>
</body>
</html>