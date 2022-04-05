package com.acgist.oauth2.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.acgist.oauth2.config.LoginType;
import com.acgist.oauth2.handler.FailCountManager;
import com.acgist.oauth2.handler.FailCountSession;

/**
 * 失败次数过滤器
 * 
 * @author acgist
 */
public class FailCountAuthenticationFilter extends OncePerRequestFilter {

	/**
	 * 匹配地址
	 */
	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login/**");
	
	/**
	 * 最大次数
	 */
	@Value("${system.login.fail.count:5}")
	private int count;
	/**
	 * 锁定时长：秒
	 */
	@Value("${system.login.fail.duration:1800}")
	private int duration;
	
	@Autowired
	private FailCountManager failCountManager;
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// 重置登陆方式
		LoginType.set(LoginType.PASSWORD);
		if (DEFAULT_ANT_PATH_REQUEST_MATCHER.matches(request)) {
			final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
			final FailCountSession failCountSession = this.failCountManager.get(username);
			if(!failCountSession.verify(this.count, this.duration)) {
				// 只有登陆成功才能清除次数：过了时间只能重试一次
				this.authenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("登陆次数限制"));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

}
