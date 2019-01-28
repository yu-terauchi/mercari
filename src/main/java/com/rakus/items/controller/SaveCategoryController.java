package com.rakus.items.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rakus.items.service.SaveCategoryService;

@RequestMapping("/insertCategory")
@Transactional
@Controller
public class SaveCategoryController {

	@Autowired
	private SaveCategoryService saveCategoryService;

//	@RequestMapping("/toSave")
//	public String toSaveCategory(Model model) {
//		saveCategory();
//		return "save";
//	}

//	@RequestMapping("/insertCategoory")
//	public void saveCategory() {
//		saveCategoryService.save();
//	}

}