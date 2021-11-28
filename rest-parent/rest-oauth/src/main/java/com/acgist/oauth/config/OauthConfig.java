package com.acgist.oauth.config;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.acgist.oauth.service.RedisAuthorizationCodeServices;
import com.acgist.oauth.service.UserService;

/**
 * 认证服务
 * 
 * 资源服务：@EnableResourceServer
 * 
 * @author acgist
 */
@Configuration
@EnableAuthorizationServer
public class OauthConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${system.secret.web:acgist}")
	private String webSecret;
	@Value("${system.secret.rest:acgist}")
	private String restSecret;
	@Value("${system.secret.validity.access:7200}")
	private int accessValidity;
	@Value("${system.secret.validity.refresh:25200}")
	private int refreshValidity;

	@Autowired
	private KeyPair jwtKeyPair;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	@Autowired
	private RedisAuthorizationCodeServices redisAuthorizationCodeServices;

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		// 认证类型：password/implicit/client_credentials/authorization_code
		configurer.inMemory()
			// Web客户端
			.withClient("client-web")
			// 认证密码
			.secret(this.passwordEncoder.encode(this.webSecret))
			// 认证类型：需要验证登陆
			.authorizedGrantTypes("authorization_code", "refresh_token")
			// 有效时间
			.accessTokenValiditySeconds(this.accessValidity).refreshTokenValiditySeconds(this.refreshValidity)
			// 自动跳转：不用确认
			.autoApprove(true).redirectUris("/oauth/code").scopes("all").and()
			// Rest客户端
			.withClient("client-rest")
			.secret(this.passwordEncoder.encode(this.restSecret))
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(this.accessValidity)
			.refreshTokenValiditySeconds(this.refreshValidity)
			.scopes("all");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer configurer) throws Exception {
		configurer
			.reuseRefreshTokens(false)
			.tokenServices(this.tokenServices())
			.userDetailsService(this.userService)
			.authenticationManager(this.authenticationManager)
			.authorizationCodeServices(this.redisAuthorizationCodeServices)
			.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer configurer) throws Exception {
		configurer
			.allowFormAuthenticationForClients()
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("permitAll()");
	}

	private AuthorizationServerTokenServices tokenServices() {
		// 签名
		final JwtAccessTokenConverter jwtTokenConverter = new JwtAccessTokenConverter();
		jwtTokenConverter.setKeyPair(this.jwtKeyPair);
		final List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
		tokenEnhancers.add(jwtTokenConverter);
		// Token增强：添加更多信息
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
		final TokenStore tokenStore = new RedisTokenStore(this.redisConnectionFactory);
		final DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(tokenStore);
		tokenServices.setTokenEnhancer(tokenEnhancerChain);
		tokenServices.setAccessTokenValiditySeconds(this.accessValidity);
		tokenServices.setRefreshTokenValiditySeconds(this.refreshValidity);
		return tokenServices;
	}

}
