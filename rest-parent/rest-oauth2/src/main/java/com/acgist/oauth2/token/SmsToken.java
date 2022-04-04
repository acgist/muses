package com.acgist.oauth2.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 短信验证码Token
 * 
 * @author acgist
 */
public class SmsToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	/**
	 * 手机号码
	 */
	private Object principal;
	/**
	 * 短信验证码
	 */
	private Object credentials;

	public SmsToken(Object mobile, Object smsCode) {
		super(null);
		this.principal = mobile;
		this.credentials = smsCode;
		super.setAuthenticated(false);
	}

	public SmsToken(Object mobile, Object smsCode, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = mobile;
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
