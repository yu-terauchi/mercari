package com.rakus.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * SpringSecurityの設定を行う.-
 * 
 * @author yu.terauchi
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	/*
	 * css,imgなどのセキュリティ設定を無視するメソッド.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter#configure(org.springframework.security.config.
	 * annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/img/**", "/js/**"); // 相対パスに変更
	}

	/**
	 * このメソッドをオーバーライドすることで、認可の設定や ログインアウトに関する設定ができる.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// TODO:もし正常に認証されなければ、許可するパスを全て追加明記する。
		// http.authorizeRequests() // 認可に関する設定 TODO:permitAll()を下にする。
		// .antMatchers("/","/login", "/showItem/index").permitAll() //
		// ログインが必要なのは、注文確認画面のみ
		// .anyRequest()
		// .authenticated(); // それ以外のパスは認証が不要

		http.authorizeRequests() // 認可に関する設定 TODO:permitAll()を下にする。
				.antMatchers(
							"/showItemsList/toItems"
							,"/editItem/toEdit"
							,"/addItem/toAdd"
							)
				.authenticated() // ログインが必要なのは、
				.anyRequest()
				.permitAll(); // それ以外のパスは認証が不要
		//		.and()
		//		.csrf()
		//		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

		http.csrf().disable();

		http.formLogin() // ログインに関する設定
				.loginPage("/login/toLogin") // ログイン画面に遷移させるパス(ログイン認証が必要なパスを指定してかつログインされていないとこのパスに遷移される)
				.permitAll()
				.loginProcessingUrl("/authenticate") // ログインボタンを押した際に遷移させるパス(ここに遷移させれば自動的にログインが行われる)?
				.failureUrl("/login/toLogin") // ログイン失敗に遷移させるパス //TODO:エラーページ作成?
				.defaultSuccessUrl("/showItemsList/toItems?currentPageId=1", true)
				// 第1引数:デフォルトでログイン成功時に遷移させるパス //分岐可能？？？
				// 第2引数: true :認証後常に第1引数のパスに遷移
				// false:認証されてなくて一度ログイン画面に飛ばされてもログインしたら指定したURLに遷移
				.usernameParameter("email") // 認証時に使用するユーザ名のリクエストパラメータ名(今回はメールアドレスを使用)
				.passwordParameter("password"); // 認証時に使用するパスワードのリクエストパラメータ名

		http.logout() // ログアウトに関する設定
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // ログアウトさせる際に遷移させるパス
				.logoutSuccessUrl("/login/toLogin") // ログアウト後に遷移させるパス(ここではログイン画面を設定)
				.deleteCookies("JSESSIONID") // ログアウト後、Cookieに保存されているセッションIDを削除
				.invalidateHttpSession(true); // true:ログアウト後、セッションを無効にする false:セッションを無効にしない

		// // Exceptionハンドラ
		// http.exceptionHandling().accessDeniedPage("/403.jsp"); // 不正なリクエストを検知しました
	}

	/**
	 * 「認証」に関する設定.<br>
	 * 認証ユーザを取得する「UserDetailsService」の設定や<br>
	 * パスワード照合時に使う「PasswordEncoder」の設定
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}

	/**
	 * <pre>
	 * bcryptアルゴリズムで暗号化する実装を返します.
	 * これを指定することでパスワード暗号化やマッチ確認する際に
	 * &#64;Autowired
	 * private PasswordEncoder passwordEncoder;
	 * と記載するとDIされるようになります。
	 * </pre>
	 * 
	 * @return bcryptアルゴリズムで暗号化する実装オブジェクト
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}