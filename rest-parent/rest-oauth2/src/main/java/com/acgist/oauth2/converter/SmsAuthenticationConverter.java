package com.acgist.oauth2.converter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;

import com.acgist.oauth2.service.SmsService;
import com.acgist.oauth2.token.SmsToken;
import com.acgist.www.utils.WebUtils;

/**
 * 短信验证码提取器
 * 
 * @author acgist
 */
public class SmsAuthenticationConverter implements AuthenticationConverter {

	@Override
	public Authentication convert(HttpServletRequest request) {
		final MultiValueMap<String, String> parameters = WebUtils.getParameters(request);
		final String grantType = parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE);
		if (!SmsToken.SMS.getValue().equals(grantType)) {
			return null;
		}
		final String mobile = parameters.getFirst("mobile");
		if (StringUtils.isEmpty(mobile)) {
			throw new AuthenticationServiceException("手机号错误");
		}
		final String smsCode = parameters.getFirst("smsCode");
		if(StringUtils.isEmpty(smsCode) || smsCode.length() != SmsService.SMS_CODE_LENGTH) {
			throw new AuthenticationServiceException("验证码错误");
		}
		final String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		final Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		return new SmsToken(scope, mobile, smsCode, clientPrincipal, parameters);
	}

}
