package com.acgist.oauth2.token;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.MultiValueMap;

import lombok.Getter;

/**
 * 密码认证Token
 * 
 * @author acgist
 */
@Getter
public class PasswordToken extends CustomToken {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 帐号
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	
	public PasswordToken(String scope, String username, String password, Authentication clientPrincipal, MultiValueMap<String, String> additionalParameters) {
		super(scope, clientPrincipal, AuthorizationGrantType.PASSWORD, additionalParameters, List.of());
		this.username = username;
		this.password = password;
		super.setAuthenticated(false);
	}
	
}
