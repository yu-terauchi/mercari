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
		List<Items> itemsList = itemsService.loadPage(model, currentPageId, request, response);
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
		// 保存してるcookie情報の取り出し
		Integer pageId = Integer.parseInt(CookieController.getCookie(request, "PageID"));
		List<Items> itemsList = itemsService.backPage(pageId);
		model.addAttribute("itemsList", itemsList);
		return "itemList";
	}
	
}
