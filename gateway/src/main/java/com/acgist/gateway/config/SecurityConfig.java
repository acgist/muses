package com.acgist.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity security) {
		security
		.csrf().disable()
		.cors().disable()
		.logout().disable()
		.httpBasic().disable()
		.formLogin().disable()
		// 解决SESSION失效：系统自动检查SESSION无效就会删除
		.requestCache().requestCache(NoOpServerRequestCache.getInstance())
		.and()
		.authorizeExchange(auth -> {
			auth
			// Rest需要认证授权
			// 授权
//		.pathMatchers("/rest/**").access(null)
			// 认证
			.pathMatchers("/rest/**").authenticated()
			// Web服务直接放行
			.anyExchange().permitAll();
//		.and()
		})
//		.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
		.oauth2ResourceServer().jwt();
//		.and()
//		// 未授权
//		.accessDeniedHandler(null)
//		// 未认证
//		.authenticationEntryPoint(null);
		return security.build();
	}
	
}