package com.acgist.oauth2.converter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MultiValueMap;

import com.acgist.oauth2.token.PasswordToken;
import com.acgist.www.utils.WebUtils;

/**
 * 短信验证码提取器
 * 
 * @author acgist
 */
public class PasswordAuthenticationConverter implements AuthenticationConverter {

	@Override
	public Authentication convert(HttpServletRequest request) {
		final MultiValueMap<String, String> parameters = WebUtils.getParameters(request);
		final String grantType = parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			return null;
		}
		final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
		if(StringUtils.isEmpty(username)) {
			throw new AuthenticationServiceException("帐号名称错误");
		}
		final String password = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
		if(StringUtils.isEmpty(password)) {
			throw new AuthenticationServiceException("帐号密码错误");
		}
		final String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		final Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		return new PasswordToken(scope, username, password, clientPrincipal, parameters);
	}

}
