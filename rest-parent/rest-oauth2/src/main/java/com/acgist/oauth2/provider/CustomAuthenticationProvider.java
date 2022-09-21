package com.acgist.oauth2.provider;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.ProviderContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.oauth2.token.CustomToken;

/**
 * 自定义验证器
 * 
 * @author acgist
 */
public abstract class CustomAuthenticationProvider<T extends CustomToken> implements AuthenticationProvider {

	private final OAuth2AuthorizationService authorizationService;
	private final OAuth2TokenGenerator<OAuth2Token> tokenGenerator;

	public CustomAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<OAuth2Token> tokenGenerator) {
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final T token = (T) authentication;
		// 认证客户
		final OAuth2ClientAuthenticationToken clientPrincipal = token.clientPrincipal();
		// 授权客户
		final RegisteredClient registeredClient = token.registeredClient();
		// 授权范围
		final Set<String> authorizedScopes = token.authorizedScopes();
		// 验证Token
		final UserDetails userDetails = this.check(token);
		if(userDetails == null) {
			throw MessageCodeException.of("无效用户");
		}
		token.authenticated(userDetails);
		// 开始创建Token
		final DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
			.principal(token)
			.providerContext(ProviderContextHolder.getProviderContext())
			.registeredClient(registeredClient)
			.authorizedScopes(authorizedScopes)
			.authorizationGrant(token)
			.authorizationGrantType(token.getAuthorizationGrantType());
		final OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName(token.getName())
			.authorizationGrantType(token.getAuthorizationGrantType())
			.attribute(Principal.class.getName(), token)
			.attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, authorizedScopes);
		// ACCESS TOKEN
		final OAuth2TokenContext accessTokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		final OAuth2Token generatedAccessToken = this.tokenGenerator.generate(accessTokenContext);
		final OAuth2AccessToken accessToken = new OAuth2AccessToken(
			OAuth2AccessToken.TokenType.BEARER,
			generatedAccessToken.getTokenValue(),
			generatedAccessToken.getIssuedAt(),
			generatedAccessToken.getExpiresAt(),
			accessTokenContext.getAuthorizedScopes()
		);
		if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
			authorizationBuilder
				.id(accessToken.getTokenValue())
				.token(accessToken, metadata -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims()))
				.attribute(Principal.class.getName(), token)
				.attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, authorizedScopes);
		} else {
			authorizationBuilder
				.id(accessToken.getTokenValue())
				.accessToken(accessToken);
		}
		// REFRESH TOKEN
		OAuth2RefreshToken refreshToken = null;
		if (
			!clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE) &&
			registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)
		) {
			final OAuth2TokenContext refreshTokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			final OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(refreshTokenContext);
			refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(refreshToken);
		}
		// 生成授权
		final OAuth2Authorization authorization = authorizationBuilder.build();
		// 保存授权
		this.authorizationService.save(authorization);
		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, Map.of());
	}
	
	/**
	 * 验证Token设置
	 * 
	 * @param token
	 * 
	 * @return 用户信息
	 */
	protected abstract UserDetails check(T token);

}
