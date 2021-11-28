package com.acgist.oauth.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;

/**
 * AuthorizationCode
 * 
 * 注意：OAuth2Authentication不能用JSON序列化
 * 
 * @author acgist
 */
@Service
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {
	
	/**
	 * 前缀
	 */
	private static final String CODE_PREFIEX = "authorization_code_";

	@Value("${system.secret.validity.code:600}")
	private int codeValidity;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	
	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		this.redisTemplate.opsForValue().set(CODE_PREFIEX + code, authentication, Duration.ofSeconds(this.codeValidity));
	}

	@Override
	protected OAuth2Authentication remove(String code) {
		final String key = CODE_PREFIEX + code;
		final OAuth2Authentication authentication = (OAuth2Authentication) this.redisTemplate.opsForValue().get(key);
		if(authentication == null) {
			return null;
		}
		this.redisTemplate.delete(key);
		return authentication;
	}

}
