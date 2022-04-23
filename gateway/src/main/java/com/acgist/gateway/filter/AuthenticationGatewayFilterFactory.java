package com.acgist.gateway.filter;

import java.text.ParseException;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.acgist.boot.model.Message;
import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.User;
import com.acgist.gateway.utils.ResponseUtils;
import com.acgist.user.api.IUserService;
import com.nimbusds.jose.JWSObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 鉴权、透传用户信息
 * 
 * @author acgist
 */
@Slf4j
@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

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
			final JWSObject jws = jws(token);
			if(jws == null) {
				final Message<String> message = Message.fail(MessageCode.CODE_3401, "没有认证");
				return ResponseUtils.response(message, HttpStatus.UNAUTHORIZED, exchange.getResponse());
			}
			// 鉴权
			final String sub = (String) jws.getPayload().toJSONObject().get("sub");
			final User user = this.userService.selectByName(sub);
			if (user == null || !user.hasPath(method, path.toString())) {
				final Message<String> message = Message.fail(MessageCode.CODE_3401, "没有权限");
				return ResponseUtils.response(message, HttpStatus.UNAUTHORIZED, exchange.getResponse());
			}
			// 设置请求用户信息：删除授权、透传用户
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
	
	/**
	 * 获取JWS信息
	 * 
	 * @param token token
	 * 
	 * @return jws
	 */
	private static final JWSObject jws(String token) {
		try {
			return JWSObject.parse(token.substring(BEARER_INDEX).strip());
		} catch (ParseException e) {
			log.error("授权信息错误：{}", token, e);
		}
		return null;
	}
	
}
