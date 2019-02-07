package com.rakus.items.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * cookie情報を取り扱うコントローラ.
 * 
 * @author yu.terauchi
 *
 */
@RequestMapping("/cookie")
@Transactional
@Controller
public class CookieController {

	/**
	 * cookie情報を取得するメソッド.
	 * 
	 * @param request リクエスト
	 * @param name cookie名
	 * @return 取得した結果
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		String result = null;
		// リクエストの中にあるcookie情報を取得
		Cookie[] cookies = request.getCookies();
		// cookie情報が存在するとき
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					result = cookie.getValue();
					break;
				}
			}
		}
		return result;
	}

	/**
	 * cookie情報をセットするメソッド.
	 * 
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param path パス
	 * @param name cookie名
	 * @param value cookieの値
	 * @param maxAge cookieの有効期限
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, 
								String path, String name,String value,int maxAge) {
		// cookie情報のセット
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		// httpsで稼働している環境であればCookieが暗号化されるようSecure属性をつける
		if ("https".equals(request.getScheme())) {
			cookie.setSecure(true);
		}
		response.addCookie(cookie);
	}
}
