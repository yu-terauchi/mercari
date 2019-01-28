package com.rakus.items.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rakus.items.domain.Category;
import com.rakus.items.domain.Items;
import com.rakus.items.form.ItemsForm;
import com.rakus.items.service.CategoryService;
import com.rakus.items.service.ItemsService;

/**
 * 商品詳細ページを表示する.
 * 
 * @author yu.terauchi
 *
 */
@RequestMapping("/showItemDetail")
@Transactional
@Controller
public class ShowItemsDetailController {

	@Autowired
	private ItemsService itemsService;
	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	public ItemsForm setUpItemsForm() {
		return new ItemsForm();
	}
	
	/**
	 * 商品詳細ページを表示する.
	 * 
	 * @param model モデル
	 * @param id 商品id
	 * @return 商品詳細情報
	 */
	@RequestMapping("/toItemDetail")
	public String toItemDetail(Model model,Integer id,Integer categoryId){
		Items itemDetail = itemsService.findItemDetail(id);
		Category grandson = categoryService.findGrandsonByItemsCategoryId(categoryId);
		model.addAttribute("grandson",grandson);
		model.addAttribute("itemDetail",itemDetail);
		return "itemDetail";
	}
}
