package com.rakus.items.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.rakus.items.controller.CookieController;
import com.rakus.items.domain.Items;
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
	// 最大表示件数
	private final int MAX_COUNT = 30;
	// 表示開始の行数 〇〇行目から表示
	private int offSetCount = 0;
	//最大ページ数
	private Integer maxPage = 1;
	// デフォルトのページID
	private Integer pageId = 1;

	/**
	 * ページIDを更新する.
	 * 
	 * @param currentPageId 現在ページID
	 * @return ぺージID
	 */
	public Integer paging(Integer currentPageId) {
		// 現在のページIDを受け取れなかったときは1ページ目が表示される ⇒ int pageId =1;
		if (currentPageId != null ) {
			pageId = currentPageId;
		}else{
			pageId = 1;
		}
		return pageId;
	}

	/**表示開始件数を計算する.
	 * @param pageId ページID
	 * @return　表示開始件数
	 */
	public Integer offSetCount(Integer pageId) {
	// 2ページ目以降　表示範囲指定
			if (pageId >= 2) {
				offSetCount = (MAX_COUNT * pageId) - 1;
			}
			return offSetCount;
	}
	
	/**フォーム入力の商品検索.
	 * @param model　モデル
	 * @param name　商品名
	 * @param parentCategoryId　親カテゴリID
	 * @param childCategoryId　子カテゴリID
	 * @param grandsonCategoryId　孫カテゴリID
	 * @param brand　ブランド名
	 * @param currentPageId　現在のページID
	 * @param request　リクエスト
	 * @param response　レスポンス
	 * @return　商品一覧
	 */
	public List<Items> findItems(Model model,
								@RequestParam(name = "name", 				required = false)	String	name,
								@RequestParam(name = "parentCategoryId",	required = false)	Integer	parentCategoryId,
								@RequestParam(name = "childCategoryId",		required = false)	Integer	childCategoryId,
								@RequestParam(name = "grandsonCategoryId", 	required = false)	Integer	grandsonCategoryId,
								@RequestParam(name = "brand", 				required = false)	String	brand, 
								@RequestParam(name = "currentPageId", 		required = false)	Integer	currentPageId,
								HttpServletRequest request, HttpServletResponse response
								) {
		pageId = paging(currentPageId);
		offSetCount = offSetCount(pageId);
		List<Items> itemsList = null;
		String message = "該当する商品がありません";

		// 空打ちで検索した場合
		if (name.isEmpty() && parentCategoryId == 0 && brand.isEmpty()) {
			pageId = Integer.parseInt(CookieController.getCookie(request, "PageID"));
			System.out.println("空打ち検索");
			itemsList = itemsRepository.loadPage(pageId,offSetCount);
			
			model.addAttribute("currentPageId", pageId);
			return itemsList;
		}
		
		// 商品名のみ
		if (!(name.isEmpty()) && parentCategoryId == 0 && brand.isEmpty()) {
			System.out.println("商品名のみの検索");
			itemsList = itemsRepository.findByName(name, pageId,offSetCount);
			maxPage = itemsRepository.countByName(name);
		}
		// ブランド名のみ
		else if (name.isEmpty() && parentCategoryId == 0 && !(brand.isEmpty())) {
			System.out.println("ブランド名のみ検索");
			itemsList = itemsRepository.findByBrand(brand, pageId,offSetCount);
			maxPage = itemsRepository.countByBrand(brand);
		}
		// 商品名・ブランド名のみ
		else if (!(name.isEmpty()) && parentCategoryId == 0 && !(brand.isEmpty())) {
			System.out.println("商品名・ブランド名検索");
			itemsList = itemsRepository.findByNameBrand(name, brand, pageId,offSetCount);
			maxPage = itemsRepository.countByNameBrand(name,brand);
		}
		// -------------------------------------------------------------------------------------------------------
		// 孫カテゴリのみ
		else if (name.isEmpty() && grandsonCategoryId != 0 && brand.isEmpty()) {
			System.out.println("孫カテゴリ検索");
			itemsList = itemsRepository.findByGrandsonCategoryId(grandsonCategoryId, pageId,offSetCount);
			maxPage = itemsRepository.countByGrandsonCategoryId(grandsonCategoryId);
		}
		// 子カテゴリのみ
		else if (name.isEmpty() && childCategoryId != 0 && grandsonCategoryId == 0 && brand.isEmpty()) {
			System.out.println("子カテゴリ検索");
			itemsList = itemsRepository.findByChildCategoryId(childCategoryId, pageId,offSetCount);
			maxPage = itemsRepository.countByChildCategoryId(childCategoryId);
		}
		// 親カテゴリのみ
		else if (name.isEmpty() && parentCategoryId != 0 && childCategoryId == 0 && brand.isEmpty()) {
			System.out.println("親カテゴリ検索");
			itemsList = itemsRepository.findByParentCategoryId(parentCategoryId, pageId,offSetCount);
			maxPage = itemsRepository.countByParentCategoryId(parentCategoryId);
		}
		// ------------------------------------------------------------------------------------------------------
		// 商品名・孫カテゴリのみ
		else if (!(name.isEmpty()) && grandsonCategoryId != 0 && brand.isEmpty()) {
			System.out.println("商品名・孫カテゴリ検索");
			itemsList = itemsRepository.findByNameGrandsonCategoryId(name, grandsonCategoryId, pageId,offSetCount);
			maxPage = itemsRepository.countByNameGrandsonCategoryId(name,grandsonCategoryId);
		}
		// 商品名・子カテゴリのみ
		else if (!(name.isEmpty()) && childCategoryId != 0 && brand.isEmpty()) {
			System.out.println("商品名・子カテゴリ検索");
			itemsList = itemsRepository.findByNameChildCategoryId(name, childCategoryId, pageId,offSetCount);
			maxPage = itemsRepository.countByNameChildCategoryId(name,childCategoryId);
		}
		// 商品名・親カテゴリのみ
		else if (!(name.isEmpty()) && parentCategoryId != 0 && brand.isEmpty()) {
			System.out.println("商品名・親カテゴリ検索");
			itemsList = itemsRepository.findByNameParentCategoryId(name, parentCategoryId, pageId,offSetCount);
			maxPage = itemsRepository.countByNameParentCategoryId(name,parentCategoryId);
		}
		//--------------------------------------------------------------------------------------------------------------------------
		//親カテゴリ・ブランド名
		else if (name.isEmpty() && parentCategoryId != 0 && !(brand.isEmpty())) {
			System.out.println("ブランド名・親カテゴリ検索");
			itemsList = itemsRepository.findByParentCategoryIdBrand(brand, parentCategoryId, pageId,offSetCount);
		}
		//子カテゴリ・ブランド名
		else if (name.isEmpty() && childCategoryId != 0 && !(brand.isEmpty())) {
			System.out.println("ブランド名・子カテゴリ検索");
			itemsList = itemsRepository.findByChildCategoryIdBrand(brand, childCategoryId, pageId,offSetCount);
		}
		//孫カテゴリ・ブランド名
		else if (name.isEmpty() && grandsonCategoryId != 0 && !(brand.isEmpty())) {
			System.out.println("ブランド名・孫カテゴリ検索");
			itemsList = itemsRepository.findByGrandsonCategoryIdBrand(brand, grandsonCategoryId, pageId,offSetCount);
		}
		// -------------------------------------------------------------------------------------------------------------------------
		// 商品名・孫カテゴリ・ブランド名
		else if (!(name.isEmpty()) && grandsonCategoryId != 0 && !(brand.isEmpty())) {
			System.out.println("商品名・孫カテゴリ・ブランド名検索");
			itemsList = itemsRepository.findByNameGrandsonCategoryIdBrand(name, grandsonCategoryId, brand, pageId,offSetCount);
			maxPage = itemsRepository.countByNameGrandsonCategoryIdBrand(name,grandsonCategoryId,brand);	
		}
		// 商品名・子カテゴリ・ブランド名
		else if (!(name.isEmpty()) && childCategoryId != 0 && grandsonCategoryId == 0 && !(brand.isEmpty())) {
			System.out.println("商品名・子カテゴリ・ブランド名検索");
			itemsList = itemsRepository.findByNameChildCategoryIdBrand(name, childCategoryId, brand, pageId,offSetCount);
			maxPage = itemsRepository.countByNameChildCategoryIdBrand(name,childCategoryId,brand);
		}
		// 商品名・親カテゴリ・ブランド名
		else if (!(name.isEmpty()) && parentCategoryId != 0 && childCategoryId == 0 && !(brand.isEmpty())) {
			System.out.println("商品名・親カテゴリ・ブランド名検索");
			itemsList = itemsRepository.findByNameParentCategoryIdBrand(name, parentCategoryId, brand, pageId,offSetCount);
			maxPage = itemsRepository.countByNameParentCategoryIdBrand(name,parentCategoryId,brand);
		}
		else {
		System.out.println("どれも引っかかってないので全件表示");
		}
		// ---------------------------------------------------------------------------------------------------------
		// 検索に引っかからなかった場合
		if (itemsList == null) {
			System.out.println("検索に引っかかってない");
			pageId = Integer.parseInt(CookieController.getCookie(request, "PageID"));
			itemsList = itemsRepository.loadPage(pageId,offSetCount);
			maxPage = itemsRepository.countPage();
			model.addAttribute("message", message);
		}
		
		//直前のページ情報の保存・Cookie情報の設定 有効期限は1日
		CookieController.setCookie(request, response, "/", "PageID", pageId.toString(), 1440 * 60);
		CookieController.setCookie(request, response, "/", "Name", name, 1440 * 60);
		CookieController.setCookie(request, response, "/", "ParentCategory", parentCategoryId.toString(), 1440 * 60);
		CookieController.setCookie(request, response, "/", "ChildCategory", childCategoryId.toString(), 1440 * 60);
		CookieController.setCookie(request, response, "/", "GrandsonCategory", grandsonCategoryId.toString(), 1440 * 60);
		CookieController.setCookie(request, response, "/", "Brand", brand, 1440 * 60);
		
		model.addAttribute("currentPageId", pageId);
		model.addAttribute("maxPage",maxPage);
		return itemsList;
	}
	
	/**リンクからの検索.
	 * @param model　モデル
	 * @param parentCategoryId　親カテゴリ
	 * @param childCategoryId　子カテゴリ
	 * @param grandsonCategoryId　孫カテゴリ
	 * @param brand　ブランド名
	 * @param currentPageId　現在のページID
	 * @param request　リクエスト
	 * @param response　レスポンス
	 * @return　商品一覧
	 */
	public List<Items> linkSearch(Model model,
								@RequestParam(name = "parentCategoryId", required = false) Integer parentCategoryId,
								@RequestParam(name = "childCategoryId", required = false)Integer childCategoryId,
								@RequestParam(name = "grandsonCategoryId", required = false)Integer grandsonCategoryId,
								@RequestParam(name = "brand", required = false)String brand, 
								@RequestParam(name = "currentPageId", required = false)Integer currentPageId,
								HttpServletRequest request,HttpServletResponse response) {
		pageId = paging(currentPageId);
		offSetCount = offSetCount(pageId);
		List<Items> itemsList = null;
		
		//親カテゴリ検索
		if(parentCategoryId != null) {
			itemsList = itemsRepository.findByParentCategoryId(parentCategoryId, currentPageId,offSetCount);
			maxPage = itemsRepository.countByParentCategoryId(parentCategoryId);
			CookieController.setCookie(request, response, "/", "ParentCategory", parentCategoryId.toString(), 1440 * 60);
		//子カテゴリ検索
		}else if(childCategoryId != null) {
			itemsList = itemsRepository.findByChildCategoryId(childCategoryId, currentPageId,offSetCount);
			maxPage = itemsRepository.countByChildCategoryId(childCategoryId);
			CookieController.setCookie(request, response, "/", "ChildCategory", childCategoryId.toString(), 1440 * 60);
		//孫カテゴリ検索
		}else if(grandsonCategoryId != null) {
			itemsList = itemsRepository.findByGrandsonCategoryId(grandsonCategoryId, currentPageId,offSetCount);
			maxPage = itemsRepository.countByGrandsonCategoryId(grandsonCategoryId);
			CookieController.setCookie(request, response, "/", "GrandsonCategory", grandsonCategoryId.toString(), 1440 * 60);
		//ブランド名検索
		}else if(brand != null) {
			itemsList = itemsRepository.findByCompleteBrand(brand, currentPageId,offSetCount);
			maxPage = itemsRepository.countByBrand(brand);
			CookieController.setCookie(request, response, "/", "Brand", brand, 1440 * 60);
		}
		CookieController.setCookie(request, response, "/", "PageID", pageId.toString(), 1440 * 60);
		model.addAttribute("maxPage",maxPage);
		model.addAttribute("currentPageId", 1);
		return itemsList;
	}
}
