package com.rakus.items.domain;

/**
 * ユーザ情報を表すエンティティ.
 * 
 * @author yu.terauchi
 *
 */
public class Users {
	/** ID */
	private Integer id;
	/** ユーザ名 */
	private String name;
	/** パスワード */
	private String password;
	/** Eメールアドレス */
	private String email;
	/** 権限 */
	private String authority;

	public Users() {
	}

	public Users(Integer id, String name, String password, String email, String authority) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", authority="
				+ authority + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
