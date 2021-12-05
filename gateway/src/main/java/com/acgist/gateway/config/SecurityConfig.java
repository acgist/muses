package com.acgist.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity security) {
		security.authorizeExchange(
			exchanges -> exchanges
			// Rest需要认证授权
			.pathMatchers("/rest/**").authenticated()
			// Web服务直接放行
			.anyExchange().permitAll()
		)
		.csrf().disable()
		.cors().disable()
		.oauth2ResourceServer().jwt();
		return security.build();
	}
}