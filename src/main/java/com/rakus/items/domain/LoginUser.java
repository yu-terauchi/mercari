package com.rakus.items.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * SpringSecurityが保持するユーザ情報を表すエンティティ.
 * @author yu.terauchi
 *
 */
public class LoginUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	/** ユーザー情報 */
	private final Users user;

	public LoginUser(Users user, Collection<GrantedAuthority> authorityList) {
		super(user.getEmail(), user.getPassword(), authorityList);
		this.user = user;
	}

	public Users getUser() {
		return user;
	}

}
