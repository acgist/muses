package com.acgist.oauth2.provider;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.oauth2.service.SmsService;
import com.acgist.oauth2.token.SmsToken;

/**
 * 短信验证码验证器
 * 
 * @author acgist
 */
public class SmsAuthenticationProvider extends CustomAuthenticationProvider<SmsToken> {

	/**
	 * 验证
	 */
	private final SmsService smsService;
	/**
	 * 用户
	 */
	private final UserDetailsService userDetailsService;
	
	public SmsAuthenticationProvider(SmsService smsService, UserDetailsService userDetailsService, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<OAuth2Token> tokenGenerator) {
		super(authorizationService, tokenGenerator);
		this.smsService = smsService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected UserDetails check(SmsToken smsToken) {
		final String mobile = smsToken.getMobile();
		final String smsCode = smsToken.getSmsCode();
		if(!this.smsService.verify(mobile, smsCode)) {
			throw MessageCodeException.of("验证码错误");
		}
		// TODO：自己实现根据手机号码查询用户
		return this.userDetailsService.loadUserByUsername(mobile);
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return SmsToken.class.isAssignableFrom(authentication);
	}

}