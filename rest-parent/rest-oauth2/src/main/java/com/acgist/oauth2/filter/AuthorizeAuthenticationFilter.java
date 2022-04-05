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

import com.acgist.oauth2.config.LoginType;
import com.acgist.oauth2.token.AuthorizeToken;

/**
 * 密码认证过滤器
 * 
 * 如果使用HttpClient需要注意重定向问题：
 * GET：正常重定向
 * POST：需要配置重定向策略
 * 
 * @author acgist
 */
public class AuthorizeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	/**
	 * 匹配地址
	 */
	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login/token");
	
	public AuthorizeAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		LoginType.set(LoginType.AUTHORIZE);
		final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
		final String password = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
		if(StringUtils.isEmpty(username)) {
			throw new AuthenticationServiceException("帐号名称错误");
		}
		if(StringUtils.isEmpty(password)) {
			throw new AuthenticationServiceException("帐号密码错误");
		}
		final AuthorizeToken authorizeToken = new AuthorizeToken(username, password);
		this.setDetails(request, authorizeToken);
		return this.getAuthenticationManager().authenticate(authorizeToken);
	}

	/**
	 * 设置请求附加信息
	 */
	protected void setDetails(HttpServletRequest request, AuthorizeToken authorizeToken) {
		// 设置跳转地址参数
		authorizeToken.setDetails(request.getQueryString());
	}
	
}
