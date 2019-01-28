package com.rakus.items.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rakus.items.domain.Items;
import com.rakus.items.form.ItemsForm;
import com.rakus.items.service.CategoryService;
import com.rakus.items.service.ItemsService;
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
	private ItemsService itemsService;
	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	public ItemsForm setUpItemsForm() {
		return new ItemsForm();
	}

	/**
	 * 検索された商品情報を読み込み商品一覧ページへフォワード.
	 * 
	 * @param model
	 *            モデル
	 * @param name
	 *            名前
	 * @return 商品一覧ページ
	 */
	@RequestMapping("/toSearchItems")
	public String SearchItems(Model model, ItemsForm form,Integer currentPageId,HttpServletRequest request,HttpServletResponse response) {
		System.out.println(form);
		List<Items> itemsList = searchItemsService.findItems(model, form,currentPageId, request,response);
		categoryService.loadParent(model);
		categoryService.loadChild(model);
		categoryService.loadGrandson(model);
		System.out.println("処理は来てる" + itemsList);
		model.addAttribute("itemsList", itemsList);
		return "itemList";
	}
}
