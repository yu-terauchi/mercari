package com.rakus.items.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rakus.items.domain.Users;
import com.rakus.items.form.UsersForm;
import com.rakus.items.service.UserService;

/**
 * 新規ユーザ登録をするコントローラ.
 * @author yu.terauchi
 *
 */
@RequestMapping("/register")
@Transactional
@Controller
public class RegisterUserController {
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ModelAttribute
	public UsersForm setUpRegisterUsersForm() {
		return new UsersForm();
	}

	/**
	 * ログイン画面へ遷移.
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}

	/**
	 * ユーザ登録画面を表示するメソッド.
	 * 
	 * @return ユーザ登録画面
	 */
	@RequestMapping("/toRegisterUser")
	public String toUserRegister(Model model) {
		return "register";
	}

	/**
	 * ユーザー登録を行い、入力内容に問題なければログイン画面を表示する.
	 * 
	 * @param リクエストパラメータ
	 * @param エラーチェック
	 * @return ログイン画面
	 */
	@RequestMapping("/registerUser")
	public String registerUser(@Validated UsersForm usersForm, BindingResult result, Model model) {

		if (!(usersForm.getPassword().equals(usersForm.getConfirmPassword()))) {
			result.rejectValue("password", null, "入力されたパスワードが異なります。");
			System.out.println("エラーチェック通過");
		}

		Users verificationUser = new Users();
		System.out.println("コントローラ内" + usersForm.getEmail());
		verificationUser = userService.findByEmail(usersForm.getEmail());
		if (verificationUser != null) {
			result.rejectValue("email", null, "メールアドレスが重複しています。");
		}

		if (result.hasErrors()) {
			return toUserRegister(model);
		}

		Users insertUser = new Users();
		// パラメータをドメインに詰め替え
		BeanUtils.copyProperties(usersForm, insertUser);
		insertUser.setAuthority("0");
		// 入力された生パスワード
		String rowPassword = usersForm.getPassword();
		// パスワードを暗号化
		String encodePassword = passwordEncoder.encode(rowPassword);
		insertUser.setPassword(encodePassword);
		userService.insert(insertUser);
		//登録が終わったらログインページへ
		return "redirect:/register/toLogin";
	}
}
