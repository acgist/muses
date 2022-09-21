package com.acgist.oauth2.token;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.MultiValueMap;

import lombok.Getter;

/**
 * 短信验证Token
 * 
 * @author acgist
 */
@Getter
public class SmsToken extends CustomToken {

	private static final long serialVersionUID = 1L;

	public static final AuthorizationGrantType SMS = new AuthorizationGrantType("sms");

	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 验证码
	 */
	private String smsCode;

	public SmsToken(String scope, String mobile, String smsCode, Authentication clientPrincipal, MultiValueMap<String, String> additionalParameters) {
		super(scope, clientPrincipal, SmsToken.SMS, additionalParameters, List.of());
		this.mobile = mobile;
		this.smsCode = smsCode;
		super.setAuthenticated(false);
	}
	
}
