package com.acgist.oauth2.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 密码认证Token：认证完成直接返回Code
 * 
 * 请求跳转参数放到details
 * 
 * @author acgist
 */
public class AuthorizeToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 帐号信息：用户名称、用户信息
	 */
	private Object principal;
	/**
	 * 登陆凭证：密码、验证码等等
	 */
	private Object credentials;
	
	public AuthorizeToken(String username, String password) {
		super(null);
		this.principal = username;
		this.credentials = password;
		super.setAuthenticated(false);
	}

	public AuthorizeToken(UserDetails user, String password, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = user;
		this.credentials = password;
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
