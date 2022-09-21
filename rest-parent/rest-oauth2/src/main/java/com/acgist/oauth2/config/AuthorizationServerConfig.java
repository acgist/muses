package com.acgist.oauth2.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.model.MessageCode;
import com.acgist.boot.utils.ErrorUtils;
import com.acgist.oauth2.converter.PasswordAuthenticationConverter;
import com.acgist.oauth2.converter.SmsAuthenticationConverter;
import com.acgist.oauth2.model.Principal;
import com.acgist.oauth2.provider.PasswordAuthenticationProvider;
import com.acgist.oauth2.provider.SmsAuthenticationProvider;
import com.acgist.oauth2.service.SmsService;
import com.acgist.oauth2.service.impl.RedisOAuth2AuthorizationConsentService;
import com.acgist.oauth2.service.impl.RedisOAuth2AuthorizationService;
import com.acgist.oauth2.token.SmsToken;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2授权服务配置
 * 
 * @author acgist
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(OAuth2Config.class)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

	@Value("${system.jwk.path:}")
	private String jwkPath;
	@Value("${system.jwk.secret:}")
	private String jwkSecret;
	
	@Autowired
	private SmsService smsService;
	@Autowired
	private OAuth2Config oAuth2Config;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JWKSource<SecurityContext> jwkSource;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private OAuth2AuthorizationService authorizationService;
	@Autowired
	private OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer;
	
	@PostConstruct
	public void init() {
		this.registerException();
	}
	
	/**
	 * 注册异常
	 */
	public void registerException() {
		ErrorUtils.register(MessageCode.CODE_3401, AuthenticationException.class);
		ErrorUtils.register(MessageCode.CODE_3401, OAuth2AuthorizationException.class);
	}
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity security) throws Exception {
//		参考：OAuth2AuthorizationServerConfiguration
		final OAuth2AuthorizationServerConfigurer<HttpSecurity> auth2AuthorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();
		// 端点匹配
		final RequestMatcher endpointsMatcher = auth2AuthorizationServerConfigurer.getEndpointsMatcher();
		// 授权方式
		auth2AuthorizationServerConfigurer.tokenEndpoint(tokenEndpointCustomizer -> {
			final DelegatingAuthenticationConverter delegatingAuthenticationConverter = new DelegatingAuthenticationConverter(
				List.of(
					new SmsAuthenticationConverter(),
					new PasswordAuthenticationConverter(),
					new OAuth2RefreshTokenAuthenticationConverter(),
					new OAuth2ClientCredentialsAuthenticationConverter(),
					new OAuth2AuthorizationCodeAuthenticationConverter(),
					new OAuth2AuthorizationCodeRequestAuthenticationConverter()
				)
			);
			// 不要注入成功失败处理逻辑
//			tokenEndpointCustomizer.errorResponseHandler(authenticationFailureHandler);
//			tokenEndpointCustomizer.accessTokenResponseHandler(authenticationSuccessHandler);
			tokenEndpointCustomizer.accessTokenRequestConverter(delegatingAuthenticationConverter);
		});
		security
			.requestMatcher(endpointsMatcher)
			.authorizeRequests(
				authorizeRequests -> authorizeRequests
//					.antMatchers("/oauth2/login").permitAll()
					.anyRequest().authenticated()
			)
			.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
			.apply(auth2AuthorizationServerConfigurer);
		// 必须设置表单登陆：登陆页面必须配置成和SecurityConfig登陆页面一致否者跳转失败
		security.formLogin();
		// 指定登陆页面
//		.loginPage("/login");
//		.loginPage("/oauth2/login");
		final OAuth2TokenGenerator<OAuth2Token> tokenGenerator = this.buildOAuth2TokenGenerator(security, this.jwkSource, this.oAuth2TokenCustomizer);
		// 授权认证
		log.info("加载短信认证");
		security.authenticationProvider(new SmsAuthenticationProvider(this.smsService, this.userDetailsService, this.authorizationService, tokenGenerator));
		log.info("加载密码认证");
		security.authenticationProvider(new PasswordAuthenticationProvider(this.passwordEncoder, this.userDetailsService, this.authorizationService, tokenGenerator));
		return security.build();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder().issuer(this.oAuth2Config.getIssuer()).build();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RedisOAuth2AuthorizationService redisOAuth2AuthorizationService() {
		return new RedisOAuth2AuthorizationService();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RedisOAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService() {
		return new RedisOAuth2AuthorizationConsentService();
	}

	/**
	 * 注意ID不能随机生成否者重启之后Redis不能正确加载
	 * 
	 * 如果需要指定重定向地址不能使用localhost等等本地回环地址
	 * 正确跳转：http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state
	 * 不能跳转：http://localhost:9999/oauth2/authorize?response_type=code&client_id=web&client_secret=acgist&scope=all&state=state&redirect_uri=http://localhost:9999/code
	 */
	@Bean
	@ConditionalOnMissingBean
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
		final TokenSettings tokenSettings = this.tokenSettings();
		final List<RegisteredClient> clients = new ArrayList<>();
		this.oAuth2Config.getClients().forEach((name, secret) -> {
			log.info("注册授权客户端：{}", name);
			final RegisteredClient client = RegisteredClient.withId(name)
				.clientId(name)
				.clientSecret(passwordEncoder.encode(secret))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.authorizationGrantType(SmsToken.SMS)
				.redirectUri(this.redirectUrl(name))
				.tokenSettings(tokenSettings)
				.scope(this.oAuth2Config.getScope())
				.build();
			clients.add(client);
		});
		return new InMemoryRegisteredClientRepository(clients);
	}
	
	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public KeyPair keyPair() {
		try (final InputStream input = this.getClass().getResourceAsStream(this.jwkPath)) {
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(input, this.jwkSecret.toCharArray());
			final Enumeration<String> aliases = keyStore.aliases();
			String aliase = null;
			while(aliases.hasMoreElements()) {
				aliase = aliases.nextElement();
				log.info("加载JWT密钥：{}", aliase);
				final PublicKey publicKey = keyStore.getCertificate(aliase).getPublicKey();
				final PrivateKey privateKey = (PrivateKey) keyStore.getKey(aliase, this.jwkSecret.toCharArray());
				return new KeyPair(publicKey, privateKey);
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
			log.error("加载JWT密钥异常：{}", this.jwkPath, e);
		}
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			log.error("随机生成JWT密钥异常", e);
		}
		return null;
	}

	@Bean
	@ConditionalOnMissingBean
	public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
		final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		final RSAKey rsaKey = new RSAKey
			.Builder(publicKey)
			.privateKey(privateKey)
			.keyID(String.valueOf(Arrays.hashCode(keyPair.getPublic().getEncoded())))
			.build();
		final JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	@ConditionalOnMissingBean
	public JwtDecoder jwtDecoder(KeyPair keyPair) {
		return NimbusJwtDecoder
			.withPublicKey((RSAPublicKey) keyPair.getPublic())
			.build();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
		return new OAuth2TokenCustomizer<JwtEncodingContext>() {
			@Override
			public void customize(JwtEncodingContext context) {
				final Authentication authentication = context.getPrincipal();
				if(authentication == null) {
					log.info("授权信息为空：{}", context);
					return;
				}
				// Token增强
				Object principal = authentication.getPrincipal();
				if(principal != null && principal instanceof Principal user) {
					context.getClaims().claim(MusesConfig.OAUTH2_ID, user.getId());
					context.getClaims().claim(MusesConfig.OAUTH2_NAME, user.getUsername());
					context.getClaims().claim(MusesConfig.OAUTH2_ROLE, String.join(",", user.getRoles()));
				}
			}
		};
	}
	
	/**
	 * @return Token生成器
	 */
	private OAuth2TokenGenerator<OAuth2Token> buildOAuth2TokenGenerator(
		HttpSecurity builder,
		JWKSource<SecurityContext> jwkSource,
		OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer
	) {
		JwtEncoder jwtEncoder = builder.getSharedObject(JwtEncoder.class);
		if (jwtEncoder == null) {
			jwtEncoder = new NimbusJwtEncoder(jwkSource);
			builder.setSharedObject(JwtEncoder.class, jwtEncoder);
		}
		JwtGenerator jwtGenerator = builder.getSharedObject(JwtGenerator.class);
		if (jwtGenerator == null) {
			jwtGenerator = new JwtGenerator(jwtEncoder);
			if (oAuth2TokenCustomizer != null) {
				jwtGenerator.setJwtCustomizer(oAuth2TokenCustomizer);
			}
			builder.setSharedObject(JwtGenerator.class, jwtGenerator);
		}
		return new DelegatingOAuth2TokenGenerator(jwtGenerator, new OAuth2AccessTokenGenerator(), new OAuth2RefreshTokenGenerator());
	}
	
	/**
	 * @return Token设置
	 */
	private TokenSettings tokenSettings() {
		return TokenSettings.builder()
			.accessTokenTimeToLive(Duration.ofSeconds(this.oAuth2Config.getAccess()))
			.refreshTokenTimeToLive(Duration.ofSeconds(this.oAuth2Config.getRefresh()))
			.build();
	}

	/**
	 * @param name 客户端名称
	 * 
	 * @return 重定向地址
	 */
	private String redirectUrl(String name) {
		final Map<String, String> redirectUris = this.oAuth2Config.getRedirectUris();
		if(redirectUris == null) {
			return this.oAuth2Config.getRedirectUri();
		}
		return redirectUris.getOrDefault(name, this.oAuth2Config.getRedirectUri());
	}
	
}
