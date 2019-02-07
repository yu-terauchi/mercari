package com.rakus.items.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.rakus.items.domain.Category;
import com.rakus.items.domain.CategoryFamily;
import com.rakus.items.repository.CategoryRepository;

/**
 * カテゴリ情報を読み込むサービス.
 * 
 * @author yu.terauchi
 *
 */
@Service
public class CategoryService {
	@Autowired
	public CategoryRepository categoryRepository;

	/**
	 * 親子孫のカテゴリ情報を一括で読み込み. return カテゴリリスト
	 */
	public List<CategoryFamily> load() {
		List<CategoryFamily> categoryFamilyList = categoryRepository.load();
		return categoryFamilyList;
	}

	/**
	 * 最小親カテゴリ情報をスコープに入れる.
	 * 
	 * @return カテゴリ一覧情報
	 */
	public void loadParent(Model model) {
		List<Category> parentList = categoryRepository.loadParent();
		model.addAttribute("parentList", parentList);
	}

	/**
	 * 最小子カテゴリ情報をスコープに入れる.
	 * 
	 * @param model
	 *            モデル
	 * @param modelMap
	 *            モデルマップ
	 */
	public void loadChild(Model model) {
		List<Category> childList = categoryRepository.loadChild();
		model.addAttribute("childList", childList);
	}

	/**
	 * 最小限孫カテゴリ情報をスコープに入れる
	 * 
	 * @param model
	 *            モデル
	 * @param modelMap
	 *            モデルマップ
	 */
	public void loadGrandson(Model model) {
		List<Category> grandsonList = categoryRepository.loadGrandson();
		model.addAttribute("grandsonList", grandsonList);
	}
	
	/**
	 *ItemsのカテゴリーIDで検索.
	 * @param categoryId 孫カテゴリID
	 * @return 孫カテゴリ一覧
	 */
	public Category findGrandsonByItemsCategoryId(Integer categoryId) {
		Category grandson = categoryRepository.findGrandsonByItemsCategoryId(categoryId);
		return grandson;
	}
	

	/**
	 * 商品idでカテゴリを検索
	 * 
	 * @param id
	 * @return
	 */
	public Category findById(Integer id) {
		Category category = categoryRepository.findById(id);
		return category;
	}

}
