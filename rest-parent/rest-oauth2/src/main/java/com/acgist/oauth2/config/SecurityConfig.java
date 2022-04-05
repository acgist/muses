package com.acgist.oauth2.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

import com.acgist.oauth2.filter.AuthorizeAuthenticationFilter;
import com.acgist.oauth2.filter.CodeAuthenticationFilter;
import com.acgist.oauth2.filter.FailCountAuthenticationFilter;
import com.acgist.oauth2.filter.SmsAuthenticationFilter;
import com.acgist.oauth2.handler.FailCountManager;
import com.acgist.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.acgist.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.acgist.oauth2.provider.AuthorizeAuthenticationProvider;
import com.acgist.oauth2.provider.SmsAuthenticationProvider;
import com.acgist.oauth2.service.SmsService;
import com.acgist.oauth2.service.impl.SmsServiceImpl;

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
	
	@Autowired
	private ApplicationContext context;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	@Order(0)
	public SecurityFilterChain oauth2SecurityFilterChain(
		HttpSecurity security,
		ProviderManager providerManager,
		AuthenticationSuccessHandler authenticationSuccessHandler,
		AuthenticationFailureHandler authenticationFailureHandler,
		FailCountAuthenticationFilter failCountAuthenticationFilter,
		AuthorizeAuthenticationFilter authorizeAuthenticationFilter,
		CodeAuthenticationFilter codeAuthenticationFilter,
		SmsAuthenticationFilter smsAuthenticationFilter
	) throws Exception {
		security
			.authorizeRequests().antMatchers("/oauth2/**").permitAll()
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/login").permitAll()
//			.anyRequest().permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			// 如果没有设置默认使用登陆页面：POST
//			.loginProcessingUrl("/login")
			.successHandler(authenticationSuccessHandler)
			.failureHandler(authenticationFailureHandler)
			.and()
			// 注意顺序
			.addFilterAt(failCountAuthenticationFilter, CsrfFilter.class)
			.addFilterAt(authorizeAuthenticationFilter, CsrfFilter.class)
			.addFilterAt(codeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//			.httpBasic();
//			.formLogin(withDefaults());
		return security.build();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ProviderManager providerManager() {
		final Collection<AuthenticationProvider> values = this.context.getBeansOfType(AuthenticationProvider.class).values();
		values.forEach(value -> log.info("添加登陆认证管理器：{}", value.getClass()));
		return new ProviderManager(values.toArray(AuthenticationProvider[]::new));
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
	
	@Bean
	@ConditionalOnMissingBean
	public AuthorizeAuthenticationProvider authorizeAuthenticationProvider() {
		return new AuthorizeAuthenticationProvider();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public FailCountAuthenticationFilter failCountAuthenticationFilter() {
		return new FailCountAuthenticationFilter();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public CodeAuthenticationFilter codeAuthenticationFilter() {
		return new CodeAuthenticationFilter();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SmsAuthenticationFilter smsAuthenticationFilter(
		ProviderManager providerManager,
		AuthenticationSuccessHandler authenticationSuccessHandler,
		AuthenticationFailureHandler authenticationFailureHandler
	) {
		final SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
		smsAuthenticationFilter.setAuthenticationManager(providerManager);
		smsAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		smsAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		return smsAuthenticationFilter;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public AuthorizeAuthenticationFilter authorizeAuthenticationFilter(
		ProviderManager providerManager,
		AuthenticationSuccessHandler authenticationSuccessHandler,
		AuthenticationFailureHandler authenticationFailureHandler
	) {
		final AuthorizeAuthenticationFilter authorizeAuthenticationFilter = new AuthorizeAuthenticationFilter();
		authorizeAuthenticationFilter.setAuthenticationManager(providerManager);
		authorizeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		authorizeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		return authorizeAuthenticationFilter;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SmsService smsService() {
		return new SmsServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public FailCountManager failCountManager() {
		return new FailCountManager();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new OAuth2AuthenticationFailureHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new OAuth2AuthenticationSuccessHandler();
	}
	
}
