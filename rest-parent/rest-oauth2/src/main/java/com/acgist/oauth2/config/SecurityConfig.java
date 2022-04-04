package com.acgist.oauth2.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.acgist.boot.UrlUtils;
import com.acgist.oauth2.filter.CodeAuthenticationFilter;
import com.acgist.oauth2.filter.FailCountAuthenticationFilter;
import com.acgist.oauth2.filter.SmsAuthenticationFilter;
import com.acgist.oauth2.provider.SmsAuthenticationProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2安全认证
 * 
 * 登陆必须包含参数：username
 * 
 * @author acgist
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Value("${system.fail.count:5}")
	private int count;
	@Value("${system.fail.duration:1800}")
	private int duration;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * 失败次数
	 */
	private Map<String, FailCountSession> failCount = new ConcurrentHashMap<>();
	
	@Bean
	@Order(0)
	public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity security, ProviderManager providerManager) throws Exception {
		// 登录成功处理器
		final AuthenticationFailureHandler authenticationFailureHandler = new OAuth2AuthenticationFailureHandler();
		// 登陆失败处理器
		final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler = new OAuth2AuthenticationSuccessHandler();
		// 失败次数过滤器
		final FailCountAuthenticationFilter failCountAuthenticationFilter = new FailCountAuthenticationFilter(this.count, this.duration, this.failCount);
		failCountAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		// 图形验证码过滤器
		final CodeAuthenticationFilter codeAuthenticationFilter = new CodeAuthenticationFilter();
		codeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		// 短信验证码过v领取
		final SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
		smsAuthenticationFilter.setAuthenticationManager(providerManager);
		smsAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		security
			.authorizeRequests().antMatchers("/oauth2/**").permitAll()
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/code", "/login", "/code.jpeg").permitAll()
//			.anyRequest().permitAll()
			.anyRequest().authenticated()
			.and()
			// 注入即可不用指定
//			.userDetailsService(this.userDetailsService)
			.formLogin()
			.loginPage("/login")
			.failureHandler(authenticationFailureHandler)
			.successHandler(authenticationSuccessHandler)
			.and()
			// 注意顺序
			.addFilterAt(failCountAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(codeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//			.httpBasic();
//			.formLogin(withDefaults());
		return security.build();
	}
	
	/**
	 * 关联验证
	 * 
	 * @param smsAuthenticationProvider 短信登陆验证
	 * @param daoAuthenticationProvider 用户密码登陆验证
	 * 
	 * @return 登陆验证管理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public ProviderManager providerManager(SmsAuthenticationProvider smsAuthenticationProvider, DaoAuthenticationProvider daoAuthenticationProvider) {
		return new ProviderManager(smsAuthenticationProvider, daoAuthenticationProvider);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SmsAuthenticationProvider smsAuthenticationProvider() {
		return new SmsAuthenticationProvider();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);
		daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
		return daoAuthenticationProvider;
	}
	
	/**
	 * 登陆失败处理器
	 * 
	 * @author acgist
	 */
	private class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
		
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
			final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
			final FailCountSession failCountSession = SecurityConfig.this.failCount.computeIfAbsent(username, key -> new FailCountSession());
			final long failCount = failCountSession.fail();
			log.info("登陆失败：{}-{}", username, failCount);
			response.sendRedirect("/login?message=" + UrlUtils.encode(exception.getMessage()));
		}
		
	};
	
	/**
	 * 登陆成功处理器
	 * 
	 * @author acgist
	 */
	private class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
		
		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
			final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
			log.debug("登陆成功：{}", username);
			SecurityConfig.this.failCount.remove(username);
			super.onAuthenticationSuccess(request, response, authentication);
		}
		
	}

}
