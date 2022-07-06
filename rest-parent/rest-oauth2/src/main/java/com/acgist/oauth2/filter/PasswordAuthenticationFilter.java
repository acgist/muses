package com.acgist.oauth2.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.acgist.oauth2.token.PasswordToken;

/**
 * 密码认证过滤器
 * 
 * @author acgist
 */
public class PasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	/**
	 * 匹配地址
	 */
	private static final AntPathRequestMatcher MATCHER = new AntPathRequestMatcher("/oauth2/password", HttpMethod.POST.name());
	
	public PasswordAuthenticationFilter() {
		super(MATCHER);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
		final String password = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
		if(StringUtils.isEmpty(username)) {
			throw new AuthenticationServiceException("帐号名称错误");
		}
		if(StringUtils.isEmpty(password)) {
			throw new AuthenticationServiceException("帐号密码错误");
		}
		final PasswordToken authorizeToken = new PasswordToken(username, password);
		this.setDetails(request, authorizeToken);
		return this.getAuthenticationManager().authenticate(authorizeToken);
	}

	/**
	 * 设置请求附加信息
	 */
	protected void setDetails(HttpServletRequest request, PasswordToken authorizeToken) {
		// 设置跳转地址参数
		authorizeToken.setDetails(request.getQueryString());
//		authorizeToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}
	
}
