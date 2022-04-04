package com.acgist.oauth2.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 图形验证码过滤器
 * 
 * @author acgist
 */
public class CodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	public static final String CODE = "code";

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
	
	public CodeAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest servletRequest = (HttpServletRequest) request;
		final HttpServletResponse servletResponse = (HttpServletResponse) response;
		if (this.requiresAuthentication(servletRequest, servletResponse)) {
			final String newCode = request.getParameter(CODE);
			final String oldCode = (String) servletRequest.getSession().getAttribute(CODE);
			if(!StringUtils.equalsIgnoreCase(newCode, oldCode)) {
				this.unsuccessfulAuthentication(servletRequest, servletResponse, new AuthenticationServiceException("图形验证码错误"));
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		return null;
	}

}
