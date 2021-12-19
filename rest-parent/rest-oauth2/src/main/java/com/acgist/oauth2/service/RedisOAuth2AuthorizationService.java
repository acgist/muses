package com.acgist.oauth2.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

import com.acgist.oauth2.config.Oauth2Config;

/**
 * 授权信息
 * 
 * @author acgist
 */
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

	@Autowired
	private Oauth2Config oauth2Config;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void save(OAuth2Authorization authorization) {
		final Optional<OAuth2AuthorizationCode> code = Optional.ofNullable(authorization.getToken(OAuth2AuthorizationCode.class))
			.map(OAuth2Authorization.Token::getToken);
		final Optional<OAuth2AccessToken> accessToken = Optional.ofNullable(authorization.getToken(OAuth2AccessToken.class))
			.map(OAuth2Authorization.Token::getToken);
		final Optional<OAuth2RefreshToken> refreshToken = Optional.ofNullable(authorization.getToken(OAuth2RefreshToken.class))
			.map(OAuth2Authorization.Token::getToken);
		if(accessToken.isEmpty() && refreshToken.isEmpty()) {
			this.redisTemplate.opsForValue().set(buildTokenKey(OAuth2ParameterNames.CODE, code.get()), authorization, this.oauth2Config.getCode(), TimeUnit.SECONDS);
			this.redisTemplate.opsForValue().set(buildKey(authorization), authorization, this.oauth2Config.getCode(), TimeUnit.SECONDS);
		} else {
			// TODO：旧的AccessToken没有删除
			this.redisTemplate.delete(buildTokenKey(OAuth2ParameterNames.CODE, code.get()));
			this.redisTemplate.opsForValue().set(buildTokenKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.get()), authorization, this.oauth2Config.getAccess(), TimeUnit.SECONDS);
			this.redisTemplate.opsForValue().set(buildTokenKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.get()), authorization, this.oauth2Config.getRefresh(), TimeUnit.SECONDS);
			this.redisTemplate.opsForValue().set(buildKey(authorization), authorization, this.oauth2Config.getRefresh(), TimeUnit.SECONDS);
		}
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Optional.ofNullable(authorization.getToken(OAuth2AuthorizationCode.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> this.redisTemplate.delete(buildTokenKey(OAuth2ParameterNames.CODE, token)));
		Optional.ofNullable(authorization.getToken(OAuth2AccessToken.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> this.redisTemplate.delete(buildTokenKey(OAuth2ParameterNames.ACCESS_TOKEN, token)));
		Optional.ofNullable(authorization.getToken(OAuth2RefreshToken.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> this.redisTemplate.delete(buildTokenKey(OAuth2ParameterNames.REFRESH_TOKEN, token)));
		this.redisTemplate.delete(buildKey(authorization));
	}

	@Override
	public OAuth2Authorization findById(String id) {
		return (OAuth2Authorization) this.redisTemplate.opsForValue().get(buildKey(id));
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		if(tokenType != null) {
			return (OAuth2Authorization) this.redisTemplate.opsForValue().get(buildTokenKey(tokenType.getValue(), token));
		} else {
			OAuth2Authorization authorization = (OAuth2Authorization) this.redisTemplate.opsForValue().get(buildTokenKey(OAuth2ParameterNames.ACCESS_TOKEN, token));
			if(authorization != null) {
				return authorization;
			}
			authorization = (OAuth2Authorization) this.redisTemplate.opsForValue().get(buildTokenKey(OAuth2ParameterNames.REFRESH_TOKEN, token));
			if(authorization != null) {
				return authorization;
			}
			authorization = (OAuth2Authorization) this.redisTemplate.opsForValue().get(buildTokenKey(OAuth2ParameterNames.CODE, token));
			if(authorization != null) {
				return authorization;
			}
		}
		return null;
	}
	
	private static final String buildKey(String id) {
		return "OA::" + id;
	}
	
	private static final String buildKey(OAuth2Authorization authorization) {
		return "OA::" + authorization.getId();
	}
	
	private static final String buildTokenKey(String type, String token) {
		return "OA::TOKEN::" + type + "::" + token;
	}
	
	private static final String buildTokenKey(String type, OAuth2Token token) {
		return buildTokenKey(type, token.getTokenValue());
	}
	
}
