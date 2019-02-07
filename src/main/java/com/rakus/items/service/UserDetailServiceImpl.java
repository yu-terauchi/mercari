package com.rakus.items.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rakus.items.domain.LoginUser;
import com.rakus.items.domain.Users;
import com.rakus.items.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	/**
	 * DBから情報を得るためのリポジトリ.
	 */
	@Autowired
	private UserRepository userRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String) loadUserByUsername(java.lang.String)
	 * DBから検索をし、ログイン情報を構成して返す。
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		System.out.println("UserDetailsServiceImplのloadUserByUsernameが呼ばれた");
		Users user = userRepository.findByEmail(email);
		if (user == null) {
//			System.out.println("そのメールアドレスは登録されていません");
			throw new UsernameNotFoundException("そのEmailは登録されていません。");
		}

		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

//		System.out.println("UserDetailsServiceImplのloadUserByUsernameが終了");
		return new LoginUser(user, authorityList);
	}
}
