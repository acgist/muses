package com.acgist.oauth2.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.acgist.oauth2.token.SmsToken;

/**
 * 短信验证码过滤器
 * 
 * @author acgist
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login/sms", "POST");
	
	public SmsAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		final String mobile = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
		final String smsCode = request.getParameter("smsCode");
		if(StringUtils.isEmpty(smsCode) || smsCode.length() != 6) {
			throw new AuthenticationServiceException("短信验证码错误");
		}
		final SmsToken smsToken = new SmsToken(mobile, smsCode);
		this.setDetails(request, smsToken);
		return this.getAuthenticationManager().authenticate(smsToken);
	}

	protected void setDetails(HttpServletRequest request, SmsToken smsToken) {
		smsToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}
	
}
