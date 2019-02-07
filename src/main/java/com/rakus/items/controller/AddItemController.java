package com.rakus.items.controller;

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
	@Autowired
	ItemsService itemsService;
	
	@ModelAttribute
	public ItemsForm setUpItemsForm() {
		return new ItemsForm();
	}
	/**
	 * 商品追加画面を表示する.
	 * 
	 * @param model モデル
	 * @return 商品追加画面
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model) {
		categoryService.loadParent(model); 
		categoryService.loadChild(model);
		categoryService.loadGrandson(model);
		return "add";
	}
	

	@RequestMapping("/toAddItem")
	public String toAddItem(@Validated ItemsForm itemsForm,BindingResult result,Model model) {
		//カテゴリの入力値チェック
		if (itemsForm.getGrandsonCategoryId() == 0) {
			result.rejectValue("grandsonCategoryId", null, "孫カテゴリーを選択してください");
		}

		if(result.hasErrors()) {
			return toAdd(model);
		}
		//Itemsにパラメータをセット
		Items items = new Items();
		items.setName(itemsForm.getName());
		items.setPrice(Integer.parseInt(itemsForm.getPrice()));
		//孫カテゴリに値が入っている場合のみセット
		if(itemsForm.getGrandsonCategoryId() != 0) {
			items.setCategoryId(itemsForm.getGrandsonCategoryId());
//		}else {
//			items.setCategoryId(null);
		}
		items.setBrand(itemsForm.getBrand());
		items.setConditionId(itemsForm.getConditionId());
		items.setDescription(itemsForm.getDescription());
		
		itemsService.addItem(items);
		
		String message = "1件の商品を追加しました";
		model.addAttribute("message" ,message);

		return "redirect:/addItem/toAdd";
	}

}
