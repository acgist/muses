package com.acgist.gateway.filter;

import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.model.Message;
import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.User;
import com.acgist.boot.utils.UrlUtils;
import com.acgist.gateway.utils.ResponseUtils;
import com.acgist.user.api.IRoleService;
import com.acgist.user.model.dto.RoleDto;
import com.nimbusds.jose.JWSObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 鉴权、透传用户信息
 * 
 * TODO：IP绑定
 * 
 * @author acgist
 */
@Slf4j
@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

	/**
	 * 授权信息
	 */
	private static final String AUTHORIZATION = "Authorization";

	/**
	 * 所有角色权限
	 * 
	 * TODO：刷新权限
	 */
	private List<RoleDto> roles;
	
	@DubboReference
	private IRoleService roleService;

	@PostConstruct
	public void init() {
		this.roles = this.roleService.all();
	}

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
			final String path = oldRequest.getPath().toString();
			final String method = oldRequest.getMethodValue();
			final String token = oldRequest.getHeaders().getFirst(AUTHORIZATION);
			final JWSObject jws = jws(token);
			if(jws == null) {
				final Message<String> message = Message.fail(MessageCode.CODE_3401, "没有认证");
				return ResponseUtils.response(message, HttpStatus.UNAUTHORIZED, exchange.getResponse());
			}
			// 鉴权
			final Long id = (Long) jws.getPayload().toJSONObject().get(MusesConfig.OAUTH2_ID);
			final String name = (String) jws.getPayload().toJSONObject().get(MusesConfig.OAUTH2_NAME);
			final String role = (String) jws.getPayload().toJSONObject().get(MusesConfig.OAUTH2_ROLE);
			final String[] roles = StringUtils.split(role, ',');
			final String authority = UrlUtils.authority(method, path);
			final boolean success = this.roles.stream()
				.filter(value -> ArrayUtils.contains(roles, value.getName()))
				.flatMap(value -> value.getPaths().stream())
				.anyMatch(value -> UrlUtils.match(authority, value));
			if (!success) {
				final Message<String> message = Message.fail(MessageCode.CODE_3401, "没有权限");
				return ResponseUtils.response(message, HttpStatus.UNAUTHORIZED, exchange.getResponse());
			}
			// 设置请求用户信息：删除授权、透传用户
			final ServerHttpRequest request = oldRequest.mutate()
				.headers(headers -> headers.remove(AUTHORIZATION))
				.header(User.HEADER_CURRENT_USER, User.current(id, name).toString())
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
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		final int index = token.indexOf(' ');
		if(index < 0) {
			return null;
		}
		try {
			return JWSObject.parse(token.substring(index + 1).strip());
		} catch (ParseException e) {
			log.error("授权信息错误：{}", token, e);
		}
		return null;
	}
	
}
