package com.acgist.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * OAuth2安全认证
 * 
 * @author acgist
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	@Order(0)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity security) throws Exception {
		security
			.authorizeRequests().antMatchers("/oauth2/**").permitAll()
			.and()
			.authorizeRequests().antMatchers("/code").permitAll()
//			.anyRequest().permitAll()
			.anyRequest().authenticated()
			.and()
			// 注入即可不用指定
//			.userDetailsService(this.userDetailsService)
			.formLogin();
//			.httpBasic();
//			.formLogin(withDefaults());
		return security.build();
	}

}
