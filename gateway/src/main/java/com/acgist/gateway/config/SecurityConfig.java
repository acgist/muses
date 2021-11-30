package com.acgist.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer configurer) throws Exception {
		configurer
			.resourceId("rest-resources")
			// 只能使用令牌
			.stateless(true);
	}

	@Override
	public void configure(HttpSecurity security) throws Exception {
		security
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/oauth/**").permitAll()
			.anyRequest().authenticated();
	}

}