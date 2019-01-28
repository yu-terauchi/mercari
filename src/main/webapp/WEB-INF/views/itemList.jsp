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
	<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" 
		integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous"/>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
		integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"/>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
		integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous"/>
	<link rel="stylesheet" href="./css/mercari.css"/>
	<!-- script -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
		integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" src="/js/category.js"></script>
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
	<script>
	//オートコンプリート機能
	$(function(){
	var names = [];
	<c:forEach var="items" items="${itemsList}">
		names.push("<c:out value="${items.name}"/>");
	</c:forEach>
		$("#autocomplete").autocomplete({
			source : names,
			minLength: 2
		});
	});
	</script>
	<title>Rakus Items</title>
</head>
<body>
	<!-- navbar -->
	<nav class="navbar navbar-inverse">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${pageContext.request.contextPath}/showItemsList/toItems?currentPageId=1">Rakus Items</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<!-- ログアウト -->
			<div>
				<ul class="nav navbar-nav navbar-right">
					<li><a id="logout" href="./login.html">Logout<i class="fa fa-power-off"></i>
					</a></li>
				</ul>
				<!-- ログイン中のユーザ名表示 -->
				<p class="navbar-text navbar-right">
					<span id="loginName">user: userName</span>
				</p>
			</div>
		</div>
	</nav>
	
	<div id="main" class="container-fluid">
		<!-- addItem link -->
		<div id="addItemButton">
			<a class="btn btn-default" href="${pageContext.request.contextPath}/addItem/toAdd"><i
				class="fa fa-plus-square-o"></i> Add New Item</a>
		</div>
		
		<!-- 検索フォーム -->
		<div id="forms">
			<form:form name="ItemsForm" modelAttribute="ItemsForm" action="${pageContext.request.contextPath}/searchItems/toSearchItems" class="form-inline" role="form">
				<!-- 商品名検索 -->
				<c:if test="${message != null}">
					<div class="text-center">
						<p>
							<c:out value="${message}" />
						</p>
					</div>
				</c:if>
				<div class="form-group">
					<input type="text" class="form-control" name="name" id="autocomplete" placeholder="name" />
				</div>
				<div class="form-group">
					<i class="fa fa-plus"></i>
				</div>
				<!-- カテゴリ検索 -->
				<div class="form-group">
					<!-- カテゴリのプルダウン表示 -->
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
				<div class="form-group"><i class="fa fa-plus"></i></div>
				<!-- ブランド名検索 -->
				<div class="form-group">
					<input type="text" name="brand" class="form-control" placeholder="brand" />
				</div>
				<div class="form-group"></div>
				<button type="submit" class="btn btn-default"><i class="fa fa-angle-double-right"></i> search</button>
			</form:form>
		</div>

		<!-- pagination -->
		<div class="pages">
			<nav class="page-nav">
				<ul class="pager">
					<li class="previous"><a href="${pageContext.request.contextPath}/showItemsList/toItems?currentPageId=<c:out value="${currentPageId - 1}"/>">&larr; prev</a></li>
					<li class="next"><a href="${pageContext.request.contextPath}/showItemsList/toItems?currentPageId=<c:out value="${currentPageId + 1}"/>">next &rarr;</a></li>
				</ul>
			</nav>
		</div>

		<!-- table -->
		<!-- 商品一覧表示部分 -->
		<div class="table-responsive">
			<table id="item-table" class="table table-hover table-condensed">
				<thead>
					<tr>
						<th>name</th>
						<th>price</th>
						<th>category</th>
						<th>brand</th>
						<th>condition</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="items" items="${itemsList}">
					<tr>
						<!-- アイテム名 -->
						<td class="item-name">
							<a href="${pageContext.request.contextPath}/showItemDetail/toItemDetail?id=<c:out value="${items.id}"/>&categoryId=<c:out value="${items.categoryId}"/>"><c:out value="${items.name}" /></a></td>
						<!-- 価格 -->
						<td class="item-price">$<c:out value="${items.price}" /></td>
						<!-- 商品カテゴリ -->
						<td class="item-category">
<%-- 						 <c:forTokens var='val' items="${items.grandson.nameAll}" delims="/"> --%>
<%-- 						 	<a href=""><c:out value="${val}" /></a> / --%>
<%-- 						 </c:forTokens> --%>
							<a href=""><c:out value="${items.parent.categoryName}" /></a> / 
							<a href=""><c:out value="${items.child.categoryName}" /></a> / 
							<a href=""><c:out value="${items.grandson.nameAll}" /></a>
						</td>
						<!-- ブランド名 -->
						<td class="item-brand">
							<a href=""><c:out value="${items.brand}" /></a>
						</td>
						<!-- 商品状態 -->
						<td class="item-condition"><c:out value="${items.conditionId}" /></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>

		<!-- pagination -->
		<div class="pages">
			<nav class="page-nav">
				<ul class="pager">
					<li class="previous"><a href="${pageContext.request.contextPath}/showItemsList/toItems?currentPageId=<c:out value="${currentPageId - 1}"/>">&larr; prev</a></li>
					<li class="next"><a href="${pageContext.request.contextPath}/showItemsList/toItems?currentPageId=<c:out value="${currentPageId + 1}"/>">next &rarr;</a></li>
				</ul>
			</nav>
			<!-- ページ番号を指定して表示するフォーム -->
			<div id="select-page">
				<form:form action="${pageContext.request.contextPath}/showItemsList/toItems" class="form-inline">
					<div class="form-group">
						<div class="input-group col-xs-6">
							<label></label>
							<input type="text" name="currentPageId" class="form-control" />
							<!-- 総ページ数 -->
							<div class="input-group-addon">/ 20</div>
						</div>
						<div class="input-group col-xs-1">
							<button type="submit" class="btn btn-default">Go</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>