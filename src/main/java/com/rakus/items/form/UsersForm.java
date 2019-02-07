package com.rakus.items.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * ユーザ情報パラメータを受け取るフォーム.
 * 
 * @author yu.terauchi
 *
 */
public class UsersForm {
	/** ユーザ名 */
	@NotBlank(message = "名前を入力してください")
	private String name;
	/** Eメールアドレス */
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "アドレスが不正です")
	private String email;
	/** パスワード */
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z\\-]{8,16}$", message = "パスワードは半角数字8~16文字で、数字、大文字小文字それぞれ１文字以上含めて入力してください")
	@NotBlank(message = "パスワードを入力してください")
	private String password;
	/** 確認用パスワード */
	@NotBlank(message = "確認用パスワードを入力してください")
	private String confirmPassword;

	public UsersForm() {
	}

	public UsersForm(@NotBlank(message = "名前を入力してください") String name,
			@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z\\-]{8,16}$", message = "パスワードは半角数字8~16文字で、数字、大文字小文字それぞれ１文字以上含めて入力してください") @NotBlank(message = "パスワードを入力してください") String password,
			@NotBlank(message = "確認用パスワードを入力してください") String confirmPassword,
			@NotBlank(message = "メールアドレスを入力してください") @Email(message = "アドレスが不正です") String email) {
		super();
		this.name = name;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.email = email;
	}

	@Override
	public String toString() {
		return "UsersForm [name=" + name + ", password=" + password + ", confirmPassword=" + confirmPassword
				+ ", email=" + email + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
