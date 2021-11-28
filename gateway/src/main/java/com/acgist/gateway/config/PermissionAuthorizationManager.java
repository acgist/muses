package com.acgist.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.nacos.client.naming.utils.RandomUtils;

import reactor.core.publisher.Mono;

@Component
public class PermissionAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionAuthorizationManager.class);
	
	/**
	 * 实现权限验证判断
	 */
	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono,
		AuthorizationContext authorizationContext) {
		ServerWebExchange exchange = authorizationContext.getExchange();
		// 请求资源
		String requestPath = exchange.getRequest().getURI().getPath();
		return authenticationMono.map(auth -> {
			return new AuthorizationDecision(checkAuthorities(exchange, auth, requestPath));
		}).defaultIfEmpty(new AuthorizationDecision(false));

	}

	// 权限校验
	private boolean checkAuthorities(ServerWebExchange exchange, Authentication auth, String requestPath) {
		Jwt principal = (Jwt) auth.getPrincipal();
		LOGGER.info("访问的URL是：{}用户信息:{}", requestPath, principal.getClaims().get("user_name"));
		return RandomUtils.nextInt() % 2 == 0;
	}

}
