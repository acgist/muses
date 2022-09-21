package com.acgist.oauth2.token;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.MultiValueMap;

import lombok.Getter;

/**
 * 自定义Token
 * 
 * @author acgist
 */
@Getter
public abstract class CustomToken extends AbstractAuthenticationToken {

	protected CustomToken(
		String scope,
		Authentication clientPrincipal,
		AuthorizationGrantType authorizationGrantType,
		MultiValueMap<String, String> additionalParameters,
		Collection<? extends GrantedAuthority> authorities
	) {
		super(authorities);
		this.scope = scope;
		this.principal = clientPrincipal;
		this.additionalParameters = additionalParameters;
		this.authorizationGrantType = authorizationGrantType;
	}

	private static final long serialVersionUID = 1L;
	
	/**
	 * 授权范围
	 */
	protected String scope;
	/**
	 * 帐号信息：用户名称、用户信息
	 */
	protected Object principal;
	/**
	 * 登陆凭证：密码、验证码等等
	 */
	protected Object credentials;
	/**
	 * 授权方式
	 */
	protected AuthorizationGrantType authorizationGrantType;
	/**
	 * 请求参数
	 */
	protected MultiValueMap<String, String> additionalParameters;
	
	/**
	 * @param principal 认证信息
	 */
	public void authenticated(UserDetails principal) {
		this.principal = principal;
		this.setAuthenticated(true);
	}
	
	/**
	 * @return 授权范围
	 */
	public Set<String> authorizedScopes() {
		final Set<String> authorizedScopes = new HashSet<>();
		if (StringUtils.isEmpty(this.getScope())) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
		} else {
			authorizedScopes.add(this.getScope());
		}
		return authorizedScopes;
	}
	
	/**
	 * @return 认证客户
	 */
	public OAuth2ClientAuthenticationToken clientPrincipal() {
		OAuth2ClientAuthenticationToken clientPrincipal = null;
		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(this.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) this.getPrincipal();
		}
		if(clientPrincipal == null) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
		}
		return clientPrincipal;
	}
	
	/**
	 * @return 授权客户
	 */
	public RegisteredClient registeredClient() {
		return this.clientPrincipal().getRegisteredClient();
	}
	
}
