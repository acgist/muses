package com.acgist.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.web.server.ServerWebExchange;

import com.acgist.boot.model.Message;
import com.acgist.boot.model.MessageCode;
import com.acgist.gateway.utils.ResponseUtils;

import reactor.core.publisher.Mono;

/**
 * 安全配置
 * 
 * @author acgist
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain gatewaySecurityFilterChain(ServerHttpSecurity security) {
		security
			.csrf().disable()
			.cors().disable()
			.logout().disable()
			.httpBasic().disable()
			.formLogin().disable()
			// 解决SESSION失效：系统自动检查SESSION无效就会删除
			.requestCache().requestCache(NoOpServerRequestCache.getInstance())
			.and()
			.authorizeExchange(authorize -> {
				authorize
				// 授权
//				.pathMatchers("/rest/**").access(null)
				// 认证
				.pathMatchers("/rest/**").authenticated()
				// 其他服务直接放行
				.anyExchange().permitAll();
			})
			.oauth2ResourceServer().jwt()
			.and()
			.accessDeniedHandler(new GatewayServerAccessDeniedHandler())
			.authenticationEntryPoint(new GatewayServerAuthenticationEntryPoint());
		return security.build();
	}
	
	/**
	 * 没有授权
	 * 
	 * @author acgist
	 */
	public static class GatewayServerAccessDeniedHandler implements ServerAccessDeniedHandler {

		@Override
		public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
			final ServerHttpResponse response = exchange.getResponse();
			if (response.isCommitted()) {
				return Mono.error(e);
			}
			final Message<String> message = Message.fail(MessageCode.CODE_3401, "没有权限");
			return ResponseUtils.response(message, HttpStatus.UNAUTHORIZED, response);
		}
		
	}

	/**
	 * 没有认证
	 * 
	 * @author acgist
	 */
	public static class GatewayServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

		@Override
		public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
			final ServerHttpResponse response = exchange.getResponse();
			if (response.isCommitted()) {
				return Mono.error(e);
			}
			final Message<String> message = Message.fail(MessageCode.CODE_3401, "没有认证");
			return ResponseUtils.response(message, HttpStatus.UNAUTHORIZED, response);
		}
		
	}
	
}