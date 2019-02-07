package com.rakus.items.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rakus.items.domain.Items;
import com.rakus.items.domain.Users;
import com.rakus.items.form.LoginForm;
import com.rakus.items.service.CategoryService;
import com.rakus.items.service.ItemsService;
import com.rakus.items.service.UserService;

@RequestMapping("/login")
@Transactional
@Controller
public class LoginController {

	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}

	/**
	 * ログイン画面を表示します.
	 * 
	 * @param model モデル
	 * @param error エラー
	 * @return ログイン画面
	 */
	@RequestMapping("/toLogin")
	public String toLogin(/** LoginForm loginForm BindingResult result, */
	Model model, @RequestParam(required = false) String error) {
		if (error != null) {
			model.addAttribute("loginError", "メールアドレスまたはパスワードが不正です。");
		}
		return "login";
	}
}
