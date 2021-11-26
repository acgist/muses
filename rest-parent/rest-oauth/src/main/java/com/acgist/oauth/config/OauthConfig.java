package com.acgist.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 授权服务
 * 
 * 资源服务：@EnableResourceServer
 * 
 * @author acgist
 */
@Configuration
@EnableAuthorizationServer
public class OauthConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${system.secret:acgist}")
	private String secret;
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer.inMemory()
			// 客户端
			.withClient("client")
			// 授权密码
			.secret(this.passwordEncoder.encode(this.secret))
			// 授权类型
			.authorizedGrantTypes("password", "refresh_token", "authorization_code")
			// 授权有效时间
			.accessTokenValiditySeconds(3600).scopes("all");
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer configurer) throws Exception {
		configurer.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			// 指定认证管理器
			.authenticationManager(authenticationManager)
			// 用户账号密码认证
			.userDetailsService(this.userService)
			// refresh_token
			.reuseRefreshTokens(false)
			// 指定token存储位置
			.tokenStore(tokenStore()).tokenServices(defaultTokenServices());
	}

	/**
	 * @return RedisTokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStore(this.redisConnectionFactory);
	}

	// 异常翻译
//	@Bean
//    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
//        return new MssWebResponseExceptionTranslator();
//    }

	@Primary
	@Bean
	public DefaultTokenServices defaultTokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		// tokenServices.setClientDetailsService(clientDetails());
		// token有效期自定义设置，默认12小时
		tokenServices.setAccessTokenValiditySeconds(60 * 60 * 24 * 7);
		// tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
		// refresh_token默认30天
		tokenServices.setAccessTokenValiditySeconds(60 * 60 * 24 * 7);
		// tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
		return tokenServices;
	}
}
