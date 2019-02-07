package com.rakus.items.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rakus.items.domain.Items;
import com.rakus.items.form.ItemsForm;
import com.rakus.items.service.CategoryService;
import com.rakus.items.service.ItemsService;

/**
 * 商品一覧情報を表示するコントローラ.
 * 
 * @author yu.terauchi
 *
 */
@RequestMapping("/showItemsList")
@Transactional
@Controller
public class ShowItemsListController {

	@Autowired
	private ItemsService itemsService;
	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	public ItemsForm setUpItemsForm() {
		return new ItemsForm();
	}

	/**
	 * 商品一覧画面を表示する.
	 * 
	 * @param model モデル
	 * @return 商品一覧画面
	 */
	@RequestMapping("/toItems")
	public String toItemsList(Model model, Integer currentPageId, HttpServletRequest request,HttpServletResponse response) {
		//cookieの値を初期化、またはcookieに保存してる検索情報をリセット(ページIDは維持)
		CookieController.setCookie(request, response, "/", "PageID","1", 1440 * 60);
		CookieController.setCookie(request, response, "/", "Name", "", 1440 * 60);
		CookieController.setCookie(request, response, "/", "ParentCategory","0",1440 * 60);
		CookieController.setCookie(request, response, "/", "ChildCategory","0", 1440 * 60);
		CookieController.setCookie(request, response, "/", "GrandsonCategory", "0", 1440 * 60);
		CookieController.setCookie(request, response, "/", "Brand", "", 1440 * 60);
		List<Items> itemsList = itemsService.loadPage(model, currentPageId,request, response);
		//検索のカテゴリプルダウン用の各カテゴリ情報を取得
		categoryService.loadParent(model);
		categoryService.loadChild(model);
		categoryService.loadGrandson(model);
		model.addAttribute("itemsList", itemsList);
		return "itemList";
	}

	/**
	 * 直前に開いていた商品一覧画面に戻る
	 * 
	 * @param model モデル
	 * @param request リクエスト
	 * @return 直前の商品一覧画面
	 */
	@RequestMapping("/backItems")
	public String backItemsList(Model model, HttpServletRequest request) {
		List<Items> itemsList = itemsService.backPage(model,request);
		//検索のカテゴリプルダウン用の各カテゴリ情報を取得
		categoryService.loadParent(model);
		categoryService.loadChild(model);
		categoryService.loadGrandson(model);
		model.addAttribute("itemsList", itemsList);
		return "itemList";
	}
	
}
