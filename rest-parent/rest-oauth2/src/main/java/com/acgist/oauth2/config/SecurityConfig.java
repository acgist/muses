package com.acgist.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Oauth2安全认证
 * 
 * @author acgist
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers("/oauth2/**").permitAll()
//			.anyRequest().permitAll()
			.anyRequest().authenticated()
			.and()
			.userDetailsService(this.userDetailsService)
			.formLogin();
//			.httpBasic();
//			.formLogin(withDefaults());
		return http.build();
	}

}
