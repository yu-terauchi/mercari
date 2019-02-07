package com.rakus.items.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.rakus.items.domain.Users;

/**
 * ユーザ情報を操作するリポジトリ.
 * @author yu.terauchi
 *
 */
@Repository
public class UserRepository {

	private final static RowMapper<Users> USERS_ROW_MAPPER = (rs, i) -> {
		Users user = new Users();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("Email"));
		user.setPassword(rs.getString("password"));
		user.setAuthority(rs.getString("authority"));
		return user;
	};
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	/**
	 * メールアドレスの１件検索を行うメソッド.
	 * 
	 * メールアドレス重複チェック、もしくはハッシュ化したパスワードチェックに使う。
	 * 
	 * @param email
	 * @return　ユーザ情報
	 */
	public Users findByEmail(String email) {
//		System.out.println("email:" + email);
		String sql = "SELECT id,name,email,password,authority FROM users WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);

		List<Users> userList = jdbcTemplate.query(sql, param, USERS_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}
	
	/**
	 * ユーザ情報を登録する.
	 * @param insertUser 新規ユーザ情報パラメータ
	 */
	public void insert(Users insertUser) {
		String sql ="INSERT INTO users(name,email,password,authority) VALUES (:name,:email,:password,:authority);";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", insertUser.getName())
															  .addValue("email", insertUser.getEmail())
															  .addValue("password", insertUser.getPassword())
															  .addValue("authority", insertUser.getAuthority());
		jdbcTemplate.update(sql, param);
	}
}
