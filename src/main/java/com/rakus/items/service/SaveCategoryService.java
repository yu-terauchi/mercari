package com.rakus.items.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rakus.items.repository.CategoryRepository;

/**
 * 商品情報を保存するコントローラ.
 * 
 * @author yu.terauchi
 *
 */
@RequestMapping("/saveCategory")
@Service
public class SaveCategoryService {
	@Autowired
	public CategoryRepository categoryRepository;

	/**
	 * 商品情報を保存する.
	 */
//	@RequestMapping("/save")
//	public void save() {
//		categoryRepository.saveCategory();
//	}
}
