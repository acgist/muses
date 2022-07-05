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

import com.acgist.oauth2.filter.CodeAuthenticationFilter;
import com.acgist.oauth2.filter.IPCountAuthenticationFilter;
import com.acgist.oauth2.filter.PasswordAuthenticationFilter;
import com.acgist.oauth2.filter.SmsAuthenticationFilter;
import com.acgist.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.acgist.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.acgist.oauth2.provider.PasswordAuthenticationProvider;
import com.acgist.oauth2.provider.SmsAuthenticationProvider;
import com.acgist.oauth2.service.IPCountService;
import com.acgist.oauth2.service.SmsService;
import com.acgist.oauth2.service.impl.IPCountServiceImpl;
import com.acgist.oauth2.service.impl.SmsServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2安全认证
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
		IPCountAuthenticationFilter ipCountAuthenticationFilter,
		PasswordAuthenticationFilter passwordAuthenticationFilter,
		SmsAuthenticationFilter smsAuthenticationFilter,
		CodeAuthenticationFilter codeAuthenticationFilter
	) throws Exception {
		security
			.authorizeRequests().antMatchers("/oauth2/**").permitAll()
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/oauth2/login").permitAll()
//			.anyRequest().permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/oauth2/login")
			// 如果没有设置默认使用登陆页面：POST
//			.loginProcessingUrl("/oauth2/login")
			.successHandler(authenticationSuccessHandler)
			.failureHandler(authenticationFailureHandler)
			.and()
			// 注意顺序
			.addFilterAt(ipCountAuthenticationFilter, CsrfFilter.class)
			.addFilterAt(passwordAuthenticationFilter, CsrfFilter.class)
			.addFilterAt(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(codeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
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
	public PasswordAuthenticationProvider passwordAuthenticationProvider() {
		return new PasswordAuthenticationProvider();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public IPCountAuthenticationFilter ipCountAuthenticationFilter() {
		return new IPCountAuthenticationFilter();
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
	public PasswordAuthenticationFilter passwordAuthenticationFilter(
		ProviderManager providerManager,
		AuthenticationSuccessHandler authenticationSuccessHandler,
		AuthenticationFailureHandler authenticationFailureHandler
	) {
		final PasswordAuthenticationFilter passwordAuthenticationFilter = new PasswordAuthenticationFilter();
		passwordAuthenticationFilter.setAuthenticationManager(providerManager);
		passwordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		passwordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		return passwordAuthenticationFilter;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SmsService smsService() {
		return new SmsServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public IPCountService ipCountService() {
		return new IPCountServiceImpl();
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
