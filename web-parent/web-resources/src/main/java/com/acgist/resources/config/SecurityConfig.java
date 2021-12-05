package com.acgist.resources.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全
 * 
 * @author acgist
 */
@Configuration
public class SecurityConfig {
	
	@Configurable
	@EnableWebSecurity
	public class AllConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity security) throws Exception {
			security
				.csrf().disable()
				.sessionManagement().disable()
				.headers().frameOptions().sameOrigin()
//				.httpStrictTransportSecurity().disable()
				.and()
				.authorizeRequests().anyRequest().permitAll();
		}
	}
	
}
