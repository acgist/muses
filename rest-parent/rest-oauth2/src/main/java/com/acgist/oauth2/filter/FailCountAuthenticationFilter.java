package com.acgist.oauth2.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.acgist.oauth2.config.FailCountSession;

/**
 * 失败次数过滤器
 * 
 * @author acgist
 */
public class FailCountAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login/**", "POST");
	
	/**
	 * 最大次数
	 */
	private final int count;
	/**
	 * 锁定时长：秒
	 */
	private final int duration;
	/**
	 * 失败次数
	 */
	private final Map<String, FailCountSession> failCount;
	
	public FailCountAuthenticationFilter(int count, int duration, Map<String, FailCountSession> failCount) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
		this.count = count;
		this.duration = duration;
		this.failCount = failCount;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest servletRequest = (HttpServletRequest) request;
		final HttpServletResponse servletResponse = (HttpServletResponse) response;
		if (this.requiresAuthentication(servletRequest, servletResponse)) {
			final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
			final FailCountSession failCountSession = this.failCount.get(username);
			if(failCountSession != null && !failCountSession.verify(this.count, this.duration)) {
				// 只有登陆成功才能清除次数：过了时间只能重试一次
				this.unsuccessfulAuthentication(servletRequest, servletResponse, new AuthenticationServiceException("登陆次数限制"));
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
