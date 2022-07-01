package com.acgist.oauth2.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.acgist.oauth2.token.AuthorizeToken;

/**
 * 密码认证验证器
 * 
 * @author acgist
 */
public class PasswordAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final AuthorizeToken token = (AuthorizeToken) authentication;
		final String username = (String) token.getPrincipal();
		final String password = (String) token.getCredentials();
		final UserDetails user = this.userDetailsService.loadUserByUsername(username);
		if(!this.passwordEncoder.matches(password, user.getPassword())) {
			throw new AuthenticationServiceException("帐号密码错误");
		}
		return new AuthorizeToken(user, password, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AuthorizeToken.class.isAssignableFrom(authentication);
	}

}