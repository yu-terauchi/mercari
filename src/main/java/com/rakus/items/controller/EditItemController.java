package com.rakus.items.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rakus.items.domain.Items;
import com.rakus.items.form.ItemsForm;
import com.rakus.items.service.CategoryService;
import com.rakus.items.service.ItemsService;

@RequestMapping("/editItem")
@Controller
@Transactional
public class EditItemController {
	
	@Autowired
	private ItemsService itemsService;
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("/toEdit")
	public String toEdit(Model model,Integer id) {
		categoryService.loadParent(model); 
		categoryService.loadChild(model);
		categoryService.loadGrandson(model);
		Items itemDetail = itemsService.findItemDetail(id);
		model.addAttribute("itemDetail",itemDetail);
		return "edit";
	}
	
	@RequestMapping("/toEditItem")
	public String toEditItem(@Validated ItemsForm itemsForm,BindingResult result,Model model,RedirectAttributes redirectAttribute) {
		//カテゴリの入力値チェック
		if (itemsForm.getGrandsonCategoryId() == 0) {
			result.rejectValue("grandsonCategoryId", null, "孫カテゴリーを選択してください");
		}

		if(result.hasErrors()) {
			return toEdit(model,itemsForm.getId());
		}
		//Itemsにパラメータをセット
		Items items = new Items();
		items.setId(itemsForm.getId());
		items.setName(itemsForm.getName());
		items.setPrice(Integer.parseInt(itemsForm.getPrice()));
		//孫カテゴリに値が入っている場合のみセット
		if(itemsForm.getGrandsonCategoryId() != 0) {
			items.setCategoryId(itemsForm.getGrandsonCategoryId());
		}
		items.setBrand(itemsForm.getBrand());
		items.setConditionId(itemsForm.getConditionId());
		items.setDescription(itemsForm.getDescription());
		
		itemsService.updateItem(items);
		//リダイレクト先にidを送る(編集した商品の詳細ページに飛ぶため)
		redirectAttribute.addAttribute("id", items.getId());
		return "redirect:/showItemDetail/toItemDetail";
	}

}
