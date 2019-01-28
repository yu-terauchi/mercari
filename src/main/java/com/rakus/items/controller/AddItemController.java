package com.rakus.items.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rakus.items.domain.Category;
import com.rakus.items.domain.CategoryFamily;
import com.rakus.items.service.CategoryService;

/**
 * 商品一覧に商品を追加するコントローラ.
 * 
 * @author yu.terauchi
 *
 */
@RequestMapping("/addItem")
@Transactional
@Controller
public class AddItemController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * 商品追加画面を表示する.
	 * 
	 * @param model モデル
	 * @return
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model) {
		return "add";
	}
	

	@RequestMapping("/toInsert")
	public String toInsert() {
		return null;
	}

}
