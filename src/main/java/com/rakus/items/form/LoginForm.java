package com.rakus.items.form;

/**
 * ユーザ情報を確認するフォーム.
 * 
 * @author yu.terauchi
 */
public class LoginForm {
	/** Eメールアドレス */
	private String email;
	/** パスワード */
	private String password;

	public LoginForm() {
	}

	public LoginForm(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginForm [email=" + email + ", password=" + password + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
