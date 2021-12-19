package com.acgist.gateway.filter;

import java.text.ParseException;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.acgist.boot.pojo.bean.User;
import com.acgist.user.service.IUserService;
import com.nimbusds.jose.JWSObject;

/**
 * 鉴权
 * 
 * @author acgist
 */
@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

	private static Logger LOGGER = LoggerFactory.getLogger(AuthenticationGatewayFilterFactory.class);

	/**
	 * 权限头部信息
	 */
	private static final String BEARER = "Bearer";
	private static final int BEARER_INDEX = BEARER.length();
	/**
	 * 授权信息
	 */
	private static final String AUTHORIZATION = "Authorization";
	
	@DubboReference
	private IUserService userService;

	public AuthenticationGatewayFilterFactory() {
		super(Config.class);
	}
	
	/**
	 * 配置
	 * 
	 * @author acgist
	 */
	public static class Config {
		
		/**
		 * 是否启用
		 */
		private boolean enable = true;

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}
		
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			if(!config.isEnable()) {
				return chain.filter(exchange);
			}
			final ServerHttpRequest oldRequest = exchange.getRequest();
			final RequestPath path = oldRequest.getPath();
			final String method = oldRequest.getMethodValue();
			final String token = oldRequest.getHeaders().getFirst(AUTHORIZATION);
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
			if (!user.hasPath(method, path.toString())) {
				// TODO：401描述
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}
			// 设置请求用户信息
			final ServerHttpRequest request = oldRequest.mutate()
				.headers(headers -> headers.remove(AUTHORIZATION))
				.header(User.HEADER_CURRENT_USER, user.currentUser().toString())
				.build();
			exchange = exchange.mutate()
				.request(request)
				.build();
			return chain.filter(exchange);
		};
	}
	
}
