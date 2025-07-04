package com.example.samuraitravel.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.samuraitravel.entity.User;

public class UserDetailsImpl implements UserDetails {

	private final User user;
	private final Collection<GrantedAuthority> authorities;

	public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}

	//
	public User getUser() {
		return user;
	}

	// ロールのコレクションを返す
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	// ハッシュ化済みのパスワードを返す
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	// ログイン時に利用するユーザー名（メールアドレス）を返す
	@Override
	public String getUsername() {
		return user.getEmail();
	}

	// アカウントが期限切れでなければtrueを返す
	public boolean isAccountNonExpired() {
		return true;
	}

	// ユーザーがロックされていなければtrueを返す
	public boolean isAccountNonLocked() {
		return true;
	}

	// ユーザーのパスワードが期限切れでなければtrueを返す
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// ユーザーが有効であればtrueを返す
	public boolean isEnabled() {
		return user.getEnabled();
	}

}

//UserDetailsImplクラス
//SpringSecurityが提供するUserDetailsインターフェースを実装したクラス
//ユーザー名（メールアドレス）やパスワード、ロールなどのユーザー情報を保持する
//Spring SecurityはこのUserDetailsオブジェクトを利用して認証や認可を行う
