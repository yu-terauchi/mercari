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

/**
 * items情報を取り扱うサービス.
 * 
 * @author yu.terauchi
 *
 */
@Service
public class ItemsService {

	@Autowired
	private ItemsRepository itemsRepository;

	/**
	 * items情報を一括で読み込むメソッド(全件一括読込のため使う機会はない).
	 * 
	 * @return 商品一覧
	 */
	public List<Items> load() {
		List<Items> itemsList = itemsRepository.load();
		return itemsList;
	}

	/**
	 * items情報をページに分けて取得する(ページング機能).
	 * 
	 * @param id ページ番号
	 * @return 商品一覧
	 */
	public List<Items> loadPage(Model model, Integer currentPageId, HttpServletRequest request,
			HttpServletResponse response) {
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
		List<Items> itemsList = itemsRepository.loadPage(pageId);

		// Cookie情報の設定 有効期限は1日
		CookieController.setCookie(request, response, "/", "PageID", pageId.toString(), 1440 * 60);
		model.addAttribute("currentPageId", pageId);
		return itemsList;
	}

	/**
	 * 直前の商品一覧画面を取得
	 * 
	 * @param pageId ページID
	 * @return 直前の商品一覧
	 */
	public List<Items> backPage(Integer pageId) {
		List<Items> itemsList = itemsRepository.loadPage(pageId);
		return itemsList;
	}

	/**
	 * 商品詳細を読み込む.
	 * 
	 * @param id 商品id
	 * @return 商品詳細情報
	 */
	public Items findItemDetail(Integer id) {
		Items itemDetail = itemsRepository.findItemsDetail(id);
		return itemDetail;
	}
}
