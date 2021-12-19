package com.acgist.oauth2.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

import com.acgist.oauth2.config.Oauth2Config;

/**
 * 授权信息
 * 
 * @author acgist
 */
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisOAuth2AuthorizationService.class);
	
	@Autowired
	private Oauth2Config oauth2Config;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void save(OAuth2Authorization authorization) {
		// 每个类型Token保存一份
		Optional.ofNullable(authorization.getToken(OAuth2AuthorizationCode.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> {
				this.redisTemplate.opsForValue().set(buildTokenKey(token), authorization, this.oauth2Config.getCode(), TimeUnit.SECONDS);
				this.redisTemplate.opsForValue().set(buildKey(authorization), authorization, this.oauth2Config.getCode(), TimeUnit.SECONDS);
			});
		Optional.ofNullable(authorization.getToken(OAuth2AccessToken.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> {
				this.redisTemplate.opsForValue().set(buildTokenKey(token), authorization, this.oauth2Config.getAccess(), TimeUnit.SECONDS);
				this.redisTemplate.opsForValue().set(buildKey(authorization), authorization, this.oauth2Config.getAccess(), TimeUnit.SECONDS);
			});
		Optional.ofNullable(authorization.getToken(OAuth2RefreshToken.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> {
				this.redisTemplate.opsForValue().set(buildTokenKey(token), authorization, this.oauth2Config.getRefresh(), TimeUnit.SECONDS);
				this.redisTemplate.opsForValue().set(buildKey(authorization), authorization, this.oauth2Config.getRefresh(), TimeUnit.SECONDS);
			});
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Optional.ofNullable(authorization.getToken(OAuth2AuthorizationCode.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> this.redisTemplate.delete(buildTokenKey(token)));
		Optional.ofNullable(authorization.getToken(OAuth2AccessToken.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> this.redisTemplate.delete(buildTokenKey(token)));
		Optional.ofNullable(authorization.getToken(OAuth2RefreshToken.class))
			.map(OAuth2Authorization.Token::getToken)
			.ifPresent(token -> this.redisTemplate.delete(buildTokenKey(token)));
		this.redisTemplate.delete(buildKey(authorization));
	}

	@Override
	public OAuth2Authorization findById(String id) {
		return (OAuth2Authorization) this.redisTemplate.opsForValue().get(buildKey(id));
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		final OAuth2Authorization authorization = (OAuth2Authorization) this.redisTemplate.opsForValue().get(buildTokenKey(token));
		if(authorization == null) {
			LOGGER.info("Token无效：{}-{}", tokenType.getValue(), token);
		}
		return authorization;
	}
	
	private static final String buildKey(String id) {
		return "OA::" + id;
	}
	
	private static final String buildKey(OAuth2Authorization authorization) {
		return "OA::" + authorization.getId();
	}
	
	private static final String buildTokenKey(String token) {
		return "OA::TOKEN::" + token;
	}
	
	private static final String buildTokenKey(OAuth2Token token) {
		return buildTokenKey(token.getTokenValue());
	}
	
}
