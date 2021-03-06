package com.acgist.oauth2.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信验证码Token
 * 
 * @author acgist
 */
public class SmsToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	/**
	 * 帐号信息：用户名称、用户信息
	 */
	private Object principal;
	/**
	 * 登陆凭证：密码、验证码等等
	 */
	private Object credentials;

	public SmsToken(String mobile, String smsCode) {
		super(null);
		this.principal = mobile;
		this.credentials = smsCode;
		super.setAuthenticated(false);
	}

	public SmsToken(UserDetails user, String smsCode, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = user;
		this.credentials = smsCode;
		super.setAuthenticated(true);
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

}
