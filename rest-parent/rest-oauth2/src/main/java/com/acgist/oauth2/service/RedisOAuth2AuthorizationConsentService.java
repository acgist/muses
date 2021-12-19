package com.acgist.oauth2.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;

import com.acgist.oauth2.config.Oauth2Config;

/**
 * 授权信息
 * 
 * @author acgist
 */
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	@Autowired
	private Oauth2Config oauth2Config;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		this.redisTemplate.opsForValue().set(buildKey(authorizationConsent), authorizationConsent, this.oauth2Config.getRefresh(), TimeUnit.SECONDS);
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		this.redisTemplate.delete(buildKey(authorizationConsent));
	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		return (OAuth2AuthorizationConsent) this.redisTemplate.opsForValue().get(buildKey(registeredClientId, principalName));
	}

	private static final String buildKey(String registeredClientId, String principalName) {
		return "OAC::" + registeredClientId + "::" + principalName;
	}

	private static final String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
		return buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}
	
}
