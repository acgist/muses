package com.acgist.gateway.filter;

import java.text.ParseException;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.acgist.boot.pojo.bean.User;
import com.acgist.user.service.IUserService;
import com.nimbusds.jose.JWSObject;

import reactor.core.publisher.Mono;

/**
 * 鉴权
 * 
 * 直接使用全局Filter
 * 
 * @author acgist
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

	private static Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

	private static final String REST = "/rest/";
	private static final int REST_INDEX = REST.length() - 1;
	private static final String BEARER = "Bearer";
	private static final int BEARER_INDEX = BEARER.length();

	@DubboReference
	private IUserService userService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		final ServerHttpRequest oldRequest = exchange.getRequest();
		final String path = oldRequest.getPath().toString();
		// 不是REST服务直接执行
		if (!path.startsWith(REST)) {
			return chain.filter(exchange);
		}
		final String token = oldRequest.getHeaders().getFirst("Authorization");
		final String method = oldRequest.getMethodValue();
		final String realPath = path.substring(REST_INDEX);
		JWSObject jws = null;
		try {
			jws = JWSObject.parse(token.substring(BEARER_INDEX).trim());
		} catch (ParseException e) {
			LOGGER.error("授权信息错误：{}", token, e);
		}
		if(jws == null) {
			// TODO：401描述
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
		final String sub = (String) jws.getPayload().toJSONObject().get("sub");
		final User user = this.userService.findByName(sub);
		if (!user.hasPath(method, realPath)) {
			// TODO：401描述
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
		// 设置请求用户信息
		final ServerHttpRequest request = oldRequest.mutate().header(User.HEADER_NAME, sub).build();
		exchange = exchange.mutate().request(request).build();
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 1;
	}

}
