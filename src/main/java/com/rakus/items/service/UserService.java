package com.rakus.items.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakus.items.domain.Users;
import com.rakus.items.repository.UserRepository;

/**
 * ユーザ情報を取り扱うサービス.
 * @author yu.terauchi
 *
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;	
	
	/**
	 * 新規ユーザ情報を登録する.
	 * @param insertUser 登録するユーザ情報
	 */
	public void insert(Users insertUser) {
		userRepository.insert(insertUser);
	}
	
	/**入力されてたアドレスがすでにあるかどうかを検証.
	 * @param email メールアドレス
	 * @return 検証対象ユーザ
	 */
	public Users findByEmail(String email) {
		Users verificationUser = userRepository.findByEmail(email);
		return verificationUser;
	}

}
