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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.acgist.oauth2.service.impl.RedisOAuth2AuthorizationConsentService;
import com.acgist.oauth2.service.impl.RedisOAuth2AuthorizationService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * OAuth2授权配置
 * 
 * @author acgist
 */
@Configuration
@EnableConfigurationProperties(OAuth2Config.class)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServerConfig.class);
	
	@Value("${system.jwk.path:}")
	private String jwkPath;
	@Value("${system.jwk.secret:}")
	private String jwkSecret;
	
	@Autowired
	private OAuth2Config oAuth2Config;
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity security) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(security);
		security.formLogin();
		return security.build();
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
		// TODO：密码模式
		final TokenSettings tokenSettings = this.tokenSettings();
		final List<RegisteredClient> clients = new ArrayList<>();
		this.oAuth2Config.getClients().forEach((name, secret) -> {
			LOGGER.info("注册授权客户端：{}", name);
			final RegisteredClient client = RegisteredClient.withId(name)
				.clientId(name)
				.clientSecret(passwordEncoder.encode(secret))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri(this.oAuth2Config.getRedirectUri())
				.tokenSettings(tokenSettings)
				.scope("all")
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
				LOGGER.info("加载JWT密钥：{}", aliase);
				final PublicKey publicKey = keyStore.getCertificate(aliase).getPublicKey();
				final PrivateKey privateKey = (PrivateKey) keyStore.getKey(aliase, this.jwkSecret.toCharArray());
				return new KeyPair(publicKey, privateKey);
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
			LOGGER.error("加载JWT密钥异常：{}", this.jwkPath, e);
		}
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("随机生成JWT密钥异常", e);
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
	
	/**
	 * @return Token设置
	 */
	private TokenSettings tokenSettings() {
		return TokenSettings.builder()
			.accessTokenTimeToLive(Duration.ofSeconds(this.oAuth2Config.getAccess()))
			.refreshTokenTimeToLive(Duration.ofSeconds(this.oAuth2Config.getRefresh()))
			.build();
	}

}
