package com.acgist.oauth2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;

import com.acgist.oauth2.filter.IPCountAuthenticationFilter;
import com.acgist.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.acgist.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.acgist.oauth2.service.IPCountService;
import com.acgist.oauth2.service.SmsService;
import com.acgist.oauth2.service.impl.IPCountServiceImpl;
import com.acgist.oauth2.service.impl.SmsServiceImpl;

/**
 * OAuth2安全认证配置
 * 
 * @author acgist
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Value("${system.error.path:/error}")
	private String errorPath;
	
	@Bean
	@Order(0)
	public SecurityFilterChain oauth2SecurityFilterChain(
		HttpSecurity security,
		IPCountAuthenticationFilter ipCountAuthenticationFilter,
		AuthenticationSuccessHandler authenticationSuccessHandler,
		AuthenticationFailureHandler authenticationFailureHandler
	) throws Exception {
		security
			.authorizeRequests()
			.antMatchers(
				// 登陆
				"/login",
				// OAuth2：AuthorizationServerConfig
				"/oauth2/**",
				// 错误
				this.errorPath,
				// 图标
				"/favicon.ico",
				// Swagger
				"/v2/api-docs", "/swagger-ui/**", "/swagger-resources/**"
			).permitAll()
			// 其余地址需要授权
			.anyRequest().authenticated()
			.and()
			.formLogin()
			// 默认不用页面授权：进攻测试
//			.loginPage("/login")
			// 如果需要页面授权需要定义controller以及页面
//			.loginPage("/oauth2/login")
			// 默认使用登陆页面：POST
//			.loginProcessingUrl("/oauth2/login")
			.successHandler(authenticationSuccessHandler)
			.failureHandler(authenticationFailureHandler)
			.and()
			// 注意顺序
			.addFilterAt(ipCountAuthenticationFilter, CsrfFilter.class);
//			.httpBasic();
//			.formLogin(Customizer.withDefaults());
		return security.build();
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
	public IPCountAuthenticationFilter ipCountAuthenticationFilter() {
		return new IPCountAuthenticationFilter();
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
