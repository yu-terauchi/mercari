package com.rakus.items.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.rakus.items.controller.CookieController;
import com.rakus.items.domain.Items;
import com.rakus.items.form.ItemsForm;
import com.rakus.items.repository.CategoryRepository;
import com.rakus.items.repository.ItemsRepository;

/**
 * 商品検索機能.
 * 
 * @author yu.terauchi
 */
@Service
public class SearchItemsService {
	@Autowired
	private ItemsRepository itemsRepository;

	/**
	 * 商品検索.
	 * @param model モデル
	 * @param form フォーム
	 * @param request リクエスト
	 * @return 商品リスト
	 */
	public List<Items> findItems(Model model,ItemsForm form,Integer currentPageId, 
			HttpServletRequest request,HttpServletResponse response) {
		
		Integer pageId = 1;
		// 現在のページIDを受け取れなかったときは1ページ目が表示される ⇒ int pageId =1;
		if (currentPageId != null) {
			pageId = currentPageId;
		} else {
			currentPageId = 1;
		}
		// 現在のページIDが0以下になることを防ぐ
		if (currentPageId <= 0) {
			currentPageId = 1;
		}
		
		String name = form.getName();
		Integer parentCategoryId = form.getParentCategoryId();
		Integer childCategoryId = form.getChildCategoryId();
		Integer grandsonCategoryId = form.getGrandsonCategoryId(); 
		String brand = form.getBrand();
		List<Items> itemsList =  itemsRepository.load();
		String message = "該当する商品がありません";
		
		// 空打ちで検索した場合
		if (name.isEmpty() && parentCategoryId == 0 && brand.isEmpty()) {
			pageId = Integer.parseInt(CookieController.getCookie(request, "PageID"));
			System.out.println("空打ち検索");
			itemsList = itemsRepository.loadPage(pageId);
		}
		//商品名のみ
		else if(!(name.isEmpty()) && parentCategoryId == 0 && brand.isEmpty()) {
			System.out.println("商品名のみの検索");
			itemsList = itemsRepository.findByName(name,pageId);
		}
		//ブランド名のみ
		else if(name.isEmpty() && parentCategoryId == 0 && !(brand.isEmpty())) {
//			itemsList = itemsRepository.findByBrand(brand);
		}
		//商品名・ブランド名のみ
		else if(!(name.isEmpty()) && parentCategoryId == 0 && !(brand.isEmpty())) {
//			itemsList = itemsRepository.findByNameBrand(name,brand);
		}
//-------------------------------------------------------------------------------------------------------
		//親カテゴリのみ
		else if(name.isEmpty() && parentCategoryId != 0 && brand.isEmpty()) {
//			itemsList = itemsRepository.findByParentCategoryId(parentCategoryId);
		}
		//子カテゴリのみ
		else if(name.isEmpty() && childCategoryId != 0 && brand.isEmpty()) {
//			itemsList = itemsRepository.findByChildCategoryId(childCategoryId);
		}
		//孫カテゴリのみ
		else if(name.isEmpty() && grandsonCategoryId != 0 && brand.isEmpty()) {
//			itemsList = itemsRepository.findByGrandsonCategoryId(grandsonCategoryId);
		}
//------------------------------------------------------------------------------------------------------
		//商品名・親カテゴリのみ
		else if(!(name.isEmpty()) && parentCategoryId != 0 && brand.isEmpty()) {
//			itemsList = itemsRepository.findByNameParentCategoryId(name,parentCategoryId);
		}
		//商品名・子カテゴリのみ
		else if(!(name.isEmpty()) && childCategoryId != 0 && brand.isEmpty()) {
//			itemsList = itemsRepository.findByNameChildCategoryId(name,childCategoryId);
		}
		//商品名・孫カテゴリのみ
		else if(!(name.isEmpty()) && grandsonCategoryId != 0 && brand.isEmpty()) {
//			itemsList = itemsRepository.findByNameGrandsonCategoryId(name,grandsonCategoryId);
		}
//---------------------------------------------------------------------------------------------------------
		//商品名・親カテゴリ・ブランド名
		else if(!(name.isEmpty()) && parentCategoryId != 0 && childCategoryId == 0 && !(brand.isEmpty())) {
//			itemsList = itemsRepository.findByNameParentCategoryIdBrand(name, parentCategoryId, brand);
		}
		//商品名・子カテゴリ・ブランド名
		else if(!(name.isEmpty()) && childCategoryId != 0 && grandsonCategoryId == 0 && !(brand.isEmpty())) {
//			itemsList = itemsRepository.findByNameChildCategoryIdBrand(name, childCategoryId, brand);
		}
		//商品名・孫カテゴリ・ブランド名
		else if(!(name.isEmpty()) && grandsonCategoryId != 0 && !(brand.isEmpty())) {
//			itemsList = itemsRepository.findByNameGrandsonCategoryIdBrand(name, grandsonCategoryId, brand);
		}else {
			System.out.println("どれも引っかかってないので全件表示");
		}
//---------------------------------------------------------------------------------------------------------
		// 検索に引っかからなかった場合
		if (itemsList == null) {
			System.out.println("検索に引っかかってない");
			pageId = Integer.parseInt(CookieController.getCookie(request, "PageID"));
			itemsList = itemsRepository.loadPage(pageId);
			model.addAttribute("message", message);
		}
		// Cookie情報の設定 有効期限は1日
		CookieController.setCookie(request, response, "/", "PageID", pageId.toString(), 1440 * 60);
		model.addAttribute("currentPageId", pageId);
		
		return itemsList;
	}





















//-------------------------------------------------------------------------------------------------------------
//	/**
//	 * 名前のみで検索.
//	 * 
//	 * @param name 商品名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByName(String name) {
//		List<Items> itemsList = itemsRepository.findByName(name);
//		return itemsList;
//	}
//	
//	/**
//	 *ブランド名のみで検索.
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByBrand(String brand) {
//		List<Items> itemsList = itemsRepository.findByBrand(brand);
//		return itemsList;
//	}
//	
//	/**
//	 * 名前・ブランド名で検索.
//	 * @param name 商品名
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNb(String name,String brand) {
//		List<Items> itemsList = itemsRepository.findByNameBrand(name,brand);
//		return itemsList;
//	}
//	
//	/**
//	 * 商品名・親カテゴリID
//	 * @param name 商品名
//	 * @param parentCategoryId 親カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNp(String name,Integer parentCategoryId) {
//		List<Items> itemsList = itemsRepository.findByNameParentCategoryId(name,parentCategoryId);
//		return itemsList;
//	}
//	
//	/**
//	 * 商品名・子カテゴリID
//	 * @param name 商品名
//	 * @param childCategoryId 子カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNc(String name,Integer childCategoryId) {
//		List<Items> itemsList = itemsRepository.findByNameChildCategoryId(name,childCategoryId);
//		return itemsList;
//	}
//	
//	/**
//	 * 商品名・孫カテゴリID
//	 * @param name 商品名
//	 * @param grandsonCategoryId 孫カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNg(String name,Integer grandsonCategoryId) {
//		List<Items> itemsList = itemsRepository.findByNameGrandsonCategoryId(name,grandsonCategoryId);
//		return itemsList;
//	}
//	
//	/**
//	 * 名前・親カテゴリID・ブランド名で検索.
//	 * 
//	 * @param name 商品名
//	 * @param parentCategoryId 親カテゴリID
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNpb(String name, Integer parentCategoryId, String brand) {
//		List<Items> itemsList = itemsRepository.findByNameParentCategoryIdBrand(name, parentCategoryId, brand);
//		return itemsList;
//	}
//	
//	/**
//	 * 名前・子カテゴリID・ブランド名で検索.
//	 * @param name 商品名
//	 * @param childCategoryId 子カテゴリID
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNcb(String name, Integer childCategoryId, String brand) {
//		List<Items> itemsList = itemsRepository.findByNameChildCategoryIdBrand(name, childCategoryId, brand);
//		return itemsList;
//	}
//	
//	/**
//	 * 名前・孫カテゴリID・ブランド名で検索.
//	 * @param name 商品名
//	 * @param grandsonCategoryId 孫カテゴリID
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNgb(String name, Integer grandsonCategoryId, String brand) {
//		List<Items> itemsList = itemsRepository.findByNameGrandsonCategoryIdBrand(name, grandsonCategoryId, brand);
//		return itemsList;
//	}
//
//	/**
//	 * 親カテゴリIDのみで検索.
//	 * @param parentCategoryId 親カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByParentCategoryId(Integer parentCategoryId) {
//		List<Items> itemsList = itemsRepository.findByParentCategoryId(parentCategoryId);
//		return itemsList;
//	}
//	
//	/**
//	 * 子カテゴリIDのみで検索.
//	 * @param childCategoryId 子カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByChildCategoryId(Integer childCategoryId) {
//		List<Items> itemsList = itemsRepository.findByChildCategoryId(childCategoryId);
//		return itemsList;
//	}
//
//	/**
//	 * 孫カテゴリIDのみで検索.
//	 * @param grandsonCategoryId 孫カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByGrandsonCategoryId(Integer grandsonCategoryId) {
//		List<Items> itemsList = itemsRepository.findByGrandsonCategoryId(grandsonCategoryId);
//		return itemsList;
//	}
}
