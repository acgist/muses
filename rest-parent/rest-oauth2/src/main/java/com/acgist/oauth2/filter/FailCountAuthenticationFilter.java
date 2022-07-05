package com.acgist.oauth2.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.acgist.oauth2.config.LoginType;
import com.acgist.oauth2.model.FailCountSession;
import com.acgist.oauth2.service.FailCountService;
import com.acgist.www.utils.WebUtils;

/**
 * 失败次数过滤器
 * 
 * @author acgist
 */
public class FailCountAuthenticationFilter extends OncePerRequestFilter {

	/**
	 * 匹配地址
	 */
	private static final AntPathRequestMatcher MATCHER = new AntPathRequestMatcher("/oauth2/login/**");
	/**
	 * 匹配登陆页面地址
	 */
	private static final AntPathRequestMatcher LOGIN_MATCHER = new AntPathRequestMatcher("/oauth2/login", HttpMethod.GET.name());
	
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
	private FailCountService failCountService;
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// 重置登陆方式
		LoginType.set(LoginType.PASSWORD);
		// 拦截所有登陆排除登陆页面
		if(LOGIN_MATCHER.matches(request)) {
			filterChain.doFilter(request, response);
		} else if (MATCHER.matches(request)) {
			final String clientIP = WebUtils.clientIP(request);
			final FailCountSession failCountSession = this.failCountService.get(clientIP);
			if(!failCountSession.verify(this.count, this.duration)) {
				// 只有登陆成功才能清除次数：过了时间只能重试一次
//				LockedException
				this.authenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("IP已被锁定"));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

}
