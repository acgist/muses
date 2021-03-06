package com.acgist.oauth2.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.acgist.oauth2.service.SmsService;
import com.acgist.oauth2.token.SmsToken;

/**
 * 短信验证码验证器
 * 
 * @author acgist
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SmsService smsService;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final SmsToken token = (SmsToken) authentication;
		final String mobile = (String) token.getPrincipal();
		final String smsCode = (String) token.getCredentials();
		if(!this.smsService.verify(mobile, smsCode)) {
			throw new AuthenticationServiceException("短信验证码错误");
		}
		// TODO：自己实现根据手机号码查询用户
		final UserDetails user = this.userDetailsService.loadUserByUsername(mobile);
		return new SmsToken(user, smsCode, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SmsToken.class.isAssignableFrom(authentication);
	}

}