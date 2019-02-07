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
<script>
//プルダウン絞り込み機能
$(function(){
	window.onload = function(){
		$('#childCategory').hide();
		$('#grandsonCategory').hide();
	}
	// 選択したidとその子供のparentidが不一致なもの
	var c_mismatch = [];
	var g_mismatch = [];
//------------------------------------------------------------------------------------------
	// 親カテゴリを選択した際
	$('#parentCategory').change(function() {
		$('#childCategory').show();
		// 親カテゴリを選びなおす際に既に非表示のものは一旦非表示を解除
		$(".hide").show();
		// 親カテゴリプルダウンで選択した値
		var p_selected_id = $('#parentCategory').val();
		
		<c:forEach var="child" items="${childList}">
			// 親カテゴリidと子カテゴリのparentIdを比較 一致しないとき
			if( p_selected_id !== "<c:out value="${child.parentId}"/>"){
				c_mismatch.push("<c:out value="${child.id}"/>");
			}
		</c:forEach>
		// 子カテゴリの要素を取得,一致しなかったchildの時実行
		for(var i = 0 ; i <= c_mismatch.length;i++){
			$(".optionChild").each(function(index,element){
			// childのidと子カテゴリプルダウンの値を比較
				if(element.value == c_mismatch[i]){
					$(element).wrap('<span class="hide">');
				}
			});
		}
		// 最終的にspanで囲まれたものを隠す
		$(".hide").hide();
		// 隠したらmismatchの中身をリセット
		c_mismatch.length = 0;
	});
//--------------------------------------------------------------------------------------------------	
	// 子カテゴリを選択した際
	$('#childCategory').change(function() {
		$('#grandsonCategory').show();
		// 子カテゴリを選びなおす際に既に非表示のものは一旦非表示を解除
		$(".hide").show();
		// 子カテゴリプルダウンで選択した値
		var c_selected_id = $('#childCategory').val();
		
		<c:forEach var="grandson" items="${grandsonList}">
			// 子カテゴリidと孫カテゴリのparentIdを比較 一致しないとき
			if( c_selected_id !== "<c:out value="${grandson.parentId}"/>"){
				g_mismatch.push("<c:out value="${grandson.id}"/>");
			}
		</c:forEach>
		
		// 子カテゴリの要素を取得,一致しなかったchildの時実行
		for(var i = 0 ; i <= g_mismatch.length;i++){
			// grandsonのidと孫カテゴリプルダウンの値を比較
			$(".optionGrandson").each(function(index,element){
				if(element.value == g_mismatch[i]){
					$(element).wrap('<span class="hide">');
				}
			});
		}
		// 最終的にspanで囲まれたもの(関係ないカテゴリ)を隠す
		$(".hide").hide();
		// 隠したらmismatchの中身をリセット(カテゴリ選択の二回目以降に備えて)
		g_mismatch.length = 0;
	});
//---------------------------------------------------------------------------------------------------
});	
</script>
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
			<a class="navbar-brand" href="${pageContext.request.contextPath}/showItemsList/toItems?currentPageId=1">Rakus Items</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<div>
				<ul class="nav navbar-nav navbar-right">
					<li><a id="logout" href="${pageContext.request.contextPath}/logout">Logout&nbsp;<i
							class="fa fa-power-off"></i></a></li>
				</ul>
				
				<sec:authorize access="hasRole('ROLE_MEMBER') and isAuthenticated()">
					<sec:authentication var="userName" property="principal.user.name" />
				</sec:authorize>&nbsp;&nbsp; 
					
				<p class="navbar-text navbar-right">
					<span id="loginName">user: <c:out value="${userName}" /></span>
				</p>
			</div>
		</div>
	</nav>

	<!-- details -->
	<div id="input-main" class="container">
		<a type="button" class="btn btn-default" href="${pageContext.request.contextPath}/showItemDetail/toItemDetail?id=<c:out value="${itemDetail.id}"/>">
		<i class="fa fa-reply"></i> back</a>
		<h2>Edit</h2>

		<!-- edit form -->
		<form:form  modelAttribute="itemsForm" action="${pageContext.request.contextPath}/editItem/toEditItem" method="POST" class="form-horizontal">
			<input type="hidden" name="id" value="${itemDetail.id}" />
			<!-- name -->
			<div class="form-group">
				<label for="inputName" class="col-sm-2 control-label">name</label>
				<div class="col-sm-8">
					<input type="text" name="name" value="${itemDetail.name}" class="form-control" id="inputName" />
					<form:errors path="name" cssStyle="color:red"  class="text-danger" />
				</div>
			</div>
			<!-- price -->
			<div class="form-group">
				<label for="price" class="col-sm-2 control-label">price</label>
				<div class="col-sm-8">
					<input type="text" name="price" value="${itemDetail.price}" class="form-control" id="price" />
					<form:errors path="price" cssStyle="color:red"  class="text-danger" />
				</div>
			</div>
			<!-- category -->
			<div class="form-group">
				<label for="category" class="col-sm-2 control-label">category</label>
				<div class="col-sm-8">
					<!-- 親カテゴリのプルダウン -->
					<select name="parentCategoryId" class="form-control" id="parentCategory">
							<option value = 0>- parentCategory -</option>
						<c:forEach var="parent" items="${parentList}">
							<option class="optionParent" value="${parent.id}" ><c:out value="${parent.categoryName}"/></option>
						</c:forEach>
					</select>
					<!-- 子カテゴリのプルダウン -->
					<select name="childCategoryId" class="form-control" id="childCategory">
							<option value = 0>- childCategory -</option>
						<c:forEach var="child" items="${childList}">
							<option class="optionChild" value="${child.id}"><c:out value="${child.categoryName}"/></option>
						</c:forEach>
					</select>
					<!-- 孫カテゴリのプルダウン -->
					<select name="grandsonCategoryId" class="form-control" id="grandsonCategory">
							<option value = 0>- grandChild -</option>
						<c:forEach var="grandson" items="${grandsonList}">
							<option class="optionGrandson" value="${grandson.id}"><c:out value="${grandson.categoryName}"/></option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="category" class="col-sm-2 control-label"></label>
				<div class="col-sm-8">
						<form:errors path="grandsonCategoryId" cssStyle="color:red" class="text-danger" />
				</div>
			</div>
			<!-- brand -->
			<div class="form-group">
				<label for="brand" class="col-sm-2 control-label">brand</label>
				<div class="col-sm-8">
					<input type="text" name="brand" id="brand" value="${itemDetail.brand}" class="form-control" name="brand" />
					<form:errors path="brand" cssStyle="color:red" class="text-danger" />
				</div>
			</div>
			<!-- condition -->
			<div class="form-group">
				<label for="condition" class="col-sm-2 control-label">condition</label>
				<div class="col-sm-8">
					<label for="condition1" class="radio-inline"> <input
						type="radio" name="conditionId" id="condition1" value="1" /> 1
					</label> <label for="condition2" class="radio-inline"> <input
						type="radio" name="conditionId" id="condition2" value="2" /> 2
					</label> <label for="condition3" class="radio-inline"> <input
						type="radio" name="conditionId" id="condition3" value="3" /> 3
					</label>
				</div>
			</div>
			<div class="form-group">
				<label for="category" class="col-sm-2 control-label"></label>
				<div class="col-sm-8">
			<form:errors path="conditionId" cssStyle="color:red" class="text-danger" />
				</div>
			</div>
			<!-- description -->
			<div class="form-group">
				<label for="description" class="col-sm-2 control-label">description</label>
				<div class="col-sm-8">
					<textarea name="description" id="description" class="form-control" rows="5"><c:out value="${itemDetail.description}"/></textarea>
					<form:errors path="description" cssStyle="color:red" class="text-danger" />
				</div>
			</div>
			<!-- submit button -->
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-default">Submit</button>
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>