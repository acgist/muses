package com.acgist.oauth2.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 图形验证码过滤器
 * 
 * @author acgist
 */
public class CodeAuthenticationFilter extends OncePerRequestFilter {
	
	/**
	 * 图形验证码参数名称
	 */
	public static final String CODE = "code";
	/**
	 * 匹配地址
	 */
	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", HttpMethod.POST.name());
	
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if(DEFAULT_ANT_PATH_REQUEST_MATCHER.matches(request)) {
			final String newCode = request.getParameter(CODE);
			final String oldCode = (String) request.getSession().getAttribute(CODE);
			if(!StringUtils.equalsIgnoreCase(newCode, oldCode)) {
				this.authenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("图形验证码错误"));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

}
