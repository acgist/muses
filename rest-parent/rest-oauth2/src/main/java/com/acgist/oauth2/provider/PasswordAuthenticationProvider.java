package com.acgist.oauth2.provider;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.oauth2.token.PasswordToken;

/**
 * 密码认证验证器
 * 
 * @author acgist
 */
public class PasswordAuthenticationProvider extends CustomAuthenticationProvider<PasswordToken> {

	/**
	 * 密码验证
	 */
	private final PasswordEncoder passwordEncoder;
	/**
	 * 用户服务
	 */
	private final UserDetailsService userDetailsService;
	
	public PasswordAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<OAuth2Token> tokenGenerator) {
		super(authorizationService, tokenGenerator);
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected UserDetails check(PasswordToken passwordToken) {
		final String username = passwordToken.getUsername();
		final String password = passwordToken.getPassword();
		final UserDetails user = this.userDetailsService.loadUserByUsername(username);
		if(!this.passwordEncoder.matches(password, user.getPassword())) {
			throw MessageCodeException.of("帐号密码错误");
		}
		return user;
	};
	
	@Override
	public boolean supports(Class<?> authentication) {
		return PasswordToken.class.isAssignableFrom(authentication);
	}

}