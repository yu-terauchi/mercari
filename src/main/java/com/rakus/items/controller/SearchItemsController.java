package com.rakus.items.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rakus.items.domain.Items;
import com.rakus.items.service.CategoryService;
import com.rakus.items.service.SearchItemsService;

/**
 * 商品情報を検索するコントローラ.
 * 
 * @author yu.terauchi
 *
 */
@RequestMapping("/searchItems")
@Transactional
@Controller
public class SearchItemsController {

	@Autowired
	private SearchItemsService searchItemsService;

	@Autowired
	private CategoryService categoryService;

	/**
	 * フォームから検索された商品情報を読み込み商品一覧ページを表示.
	 * @param model モデル
	 * @param name 名前
	 * @return 商品一覧ページ
	 */
	@RequestMapping("/toSearchItems")
	public String searchItems(Model model,	
							@RequestParam(name = "name", required = false) String name,
							@RequestParam(name = "parentCategoryId", required = false) Integer parentCategoryId,
							@RequestParam(name = "childCategoryId", required = false)Integer childCategoryId,
							@RequestParam(name = "grandsonCategoryId", required = false)Integer grandsonCategoryId,
							@RequestParam(name = "brand", required = false)String brand, 
							@RequestParam(name = "currentPageId", required = false)Integer currentPageId,
							HttpServletRequest request,HttpServletResponse response) {
		List<Items> itemsList = searchItemsService.findItems(model, name, parentCategoryId, childCategoryId,
															grandsonCategoryId, brand, currentPageId, request, response);
		categoryService.loadParent(model);
		categoryService.loadChild(model);
		categoryService.loadGrandson(model);
		System.out.println("処理は来てる" + itemsList);
		model.addAttribute("itemsList", itemsList);
		return "itemList";
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * リンクから検索された商品情報を読み込み商品一覧ページを表示.
	 * @param model　モデル
	 * @param name　商品名
	 * @param parentCategoryId　親カテゴリ
	 * @param childCategoryId　子カテゴリ
	 * @param grandsonCategoryId　孫カテゴリ
	 * @param brand　ブランド名
	 * @param currentPageId　現在のページID
	 * @param request　リクエスト
	 * @param response　レスポンス
	 * @return　商品一覧
	 */
	@RequestMapping("/toLinkSearch")
	public String linkSearch(Model model,
							@RequestParam(name = "parentCategoryId",	required = false)	Integer parentCategoryId,
							@RequestParam(name = "childCategoryId", 	required = false)	Integer childCategoryId,
							@RequestParam(name = "grandsonCategoryId", 	required = false)	Integer grandsonCategoryId,
							@RequestParam(name = "brand", 				required = false)	String brand, 
							@RequestParam(name = "currentPageId", 		required = false)	Integer currentPageId,
							HttpServletRequest request,HttpServletResponse response) {
		List<Items> itemsList = searchItemsService.linkSearch(model, 
															  parentCategoryId,
															  childCategoryId,
															  grandsonCategoryId,
															  brand,
															  currentPageId,
															  request, response );
		categoryService.loadParent(model);
		categoryService.loadChild(model);
		categoryService.loadGrandson(model);
		model.addAttribute("itemsList", itemsList);
		return "itemList";
	}

}
