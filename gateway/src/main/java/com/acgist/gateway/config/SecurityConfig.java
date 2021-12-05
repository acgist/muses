package com.acgist.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity security) {
		security.authorizeExchange(
			exchanges -> exchanges
			// Rest需要认证授权
//			.pathMatchers("/rest/**").access(null) // 授权
			.pathMatchers("/rest/**").authenticated() // 认证
			// Web服务直接放行
			.anyExchange().permitAll()
		)
		.csrf().disable()
		.cors().disable()
		.oauth2ResourceServer().jwt();
//		.and()
//		// 未授权
//		.accessDeniedHandler(null)
//		// 未认证
//		.authenticationEntryPoint(null);
		return security.build();
	}
	
}