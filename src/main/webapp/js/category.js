$(function(){
	// 選択したidとその子供のparentidが不一致なもの
	var c_mismatch = [];
	var g_mismatch = [];
//------------------------------------------------------------------------------------------
	// 親カテゴリを選択した際
	$('#parentCategory').change(function() {
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
		// 子カテゴリの要素と上のmismatchが一致するものを非表示に
		$(".optionChild").each(function(index,element){
			// childのidと子カテゴリプルダウンの値を比較
			for(var i = 0 ; i <= c_mismatch.length;i++){
				if(element.value == c_mismatch[i]){
					$(element).wrap('<span class="hide">');
				}
			}
		});
		// 最終的にspanで囲まれたものを隠す
		$(".hide").hide();
		// 隠したらmismatchの中身をリセット
		c_mismatch.length = 0;
	});
//--------------------------------------------------------------------------------------------------	
	// 子カテゴリを選択した際
	$('#childCategory').change(function() {
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
		
		// 孫カテゴリの要素と上のmismatchが一致するものを非表示に
		$(".optionGrandson").each(function(index,element){
			for(var i = 0 ; i <= g_mismatch.length;i++){
				if(element.value == g_mismatch[i]){
					$(element).wrap('<span class="hide">');
				}
			}
		});
		// 最終的にspanで囲まれたもの(関係ないカテゴリ)を隠す
		$(".hide").hide();
		// 隠したらmismatchの中身をリセット(カテゴリ選択の二回目以降に備えて)
		g_mismatch.length = 0;
	});
//---------------------------------------------------------------------------------------------------
});	