package com.rakus.items.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.rakus.items.controller.CookieController;
import com.rakus.items.domain.Items;
import com.rakus.items.repository.ItemsRepository;

/**items情報を取り扱うサービス.
 * @author yu.terauchi
 */
@Service
public class ItemsService {

	@Autowired
	private ItemsRepository itemsRepository;
	@Autowired
	private SearchItemsService searchItemsService;

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
	 * @param currentPageId 現在ページID
	 * @return ぺージID
	 */
	public Integer paging(Integer currentPageId) {
		// 現在のページIDを受け取れなかったときは1ページ目が表示される ⇒ int pageId =1;
		if (currentPageId != null) {
			pageId = currentPageId;
		} else {
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
	
	/**
	 * 商品一覧を取得.
	 * @param id ページ番号
	 * @return 商品一覧
	 */
	public List<Items> loadPage(Model model, Integer currentPageId, HttpServletRequest request,HttpServletResponse response) {
		String name = CookieController.getCookie(request, "Name");
		Integer parentCategoryId = Integer.parseInt(CookieController.getCookie(request, "ParentCategory"));
		Integer childCategoryId = Integer.parseInt(CookieController.getCookie(request, "ChildCategory"));
		Integer grandsonCategoryId = Integer.parseInt(CookieController.getCookie(request, "GrandsonCategory"));
		String brand = CookieController.getCookie(request, "Brand");
		//cookieが初期状態ではない場合
		if(!(name.isEmpty() && parentCategoryId == 0 && childCategoryId == 0 && grandsonCategoryId == 0 && brand.isEmpty())) {
			return searchItemsService.findItems(model, name, parentCategoryId, childCategoryId, grandsonCategoryId, brand, currentPageId, request, response);
		}
		pageId = paging(currentPageId);
		offSetCount = offSetCount(pageId);
		List<Items> itemsList = itemsRepository.loadPage(pageId,offSetCount);
		maxPage = itemsRepository.countPage();
		// Cookie情報の設定 有効期限は1日
		CookieController.setCookie(request, response, "/", "PageID", pageId.toString(), 1440 * 60);
		model.addAttribute("maxPage",maxPage);
		model.addAttribute("currentPageId", pageId);
		return itemsList;
	}

	/**直前の商品一覧画面を取得
	 * @param pageId ページID
	 * @return 直前の商品一覧
	 */
	public List<Items> backPage(Model model,HttpServletRequest request) {
		List<Items> itemsList = null;
		// 保存してるcookie情報の取り出し
		Integer pageId = Integer.parseInt(CookieController.getCookie(request, "PageID"));
		String name = CookieController.getCookie(request, "Name");
		Integer parentCategoryId = Integer.parseInt(CookieController.getCookie(request, "ParentCategory"));
		Integer childCategoryId = Integer.parseInt(CookieController.getCookie(request, "ChildCategory"));
		Integer grandsonCategoryId = Integer.parseInt(CookieController.getCookie(request, "GrandsonCategory"));
		String brand = CookieController.getCookie(request, "Brand");
		//表示開始件数の取得
		offSetCount = offSetCount(pageId);
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
		// ---------------------------------------------------------------------------------------------------------
		// 商品名・孫カテゴリ・ブランド名
		else if (!(name.isEmpty()) && grandsonCategoryId != 0 && !(brand.isEmpty())) {
			System.out.println("商品名・孫カテゴリ・ブランド名検索");
			itemsList = itemsRepository.findByNameGrandsonCategoryIdBrand(name, grandsonCategoryId, brand, pageId,offSetCount);
			maxPage = itemsRepository.countByNameGrandsonCategoryIdBrand(name,grandsonCategoryId,brand);
		// 商品名・子カテゴリ・ブランド名
		} else if (!(name.isEmpty()) && childCategoryId != 0 && grandsonCategoryId == 0 && !(brand.isEmpty())) {
			System.out.println("商品名・子カテゴリ・ブランド名検索");
			itemsList = itemsRepository.findByNameChildCategoryIdBrand(name, childCategoryId, brand, pageId,offSetCount);
			maxPage = itemsRepository.countByNameChildCategoryIdBrand(name,childCategoryId,brand);
		}
		// 商品名・親カテゴリ・ブランド名
		else if (!(name.isEmpty()) && parentCategoryId != 0 && childCategoryId == 0 && !(brand.isEmpty())) {
			System.out.println("商品名・親カテゴリ・ブランド名検索");
			itemsList = itemsRepository.findByNameParentCategoryIdBrand(name, parentCategoryId, brand, pageId,offSetCount);
			maxPage = itemsRepository.countByNameParentCategoryIdBrand(name,parentCategoryId,brand);
		}else {
			itemsList = itemsRepository.loadPage(pageId,offSetCount);
			maxPage = itemsRepository.countPage();
		}
		model.addAttribute("maxPage",maxPage);
		model.addAttribute("currentPageId", pageId);
		return itemsList;
	}
//-------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 商品詳細を取得.
	 * @param id 商品id
	 * @return 商品詳細情報
	 */
	public Items findItemDetail(Integer id) {
		Items itemDetail = itemsRepository.findItemsDetail(id);
		return itemDetail;
	}

// -------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 新規商品追加.
	 * @param item 1件の商品情報
	 */
	public void addItem(Items item) {
		itemsRepository.addItem(item);
	}

	/**
	 * 商品情報編集.
	 * @param item 1件の商品情報
	 */
	public void updateItem(Items item) {
		itemsRepository.updateItem(item);
	}
}
