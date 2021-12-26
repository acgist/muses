package com.acgist.resources.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 安全
 * 
 * @author acgist
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity security) throws Exception {
		security
			.csrf().disable()
			.headers().frameOptions().sameOrigin()
			.and()
			.authorizeRequests().anyRequest().permitAll()
			.and()
//			.sessionManagement().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			.httpStrictTransportSecurity().disable();
	}
	
}