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
import java.util.Arrays;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.acgist.oauth2.service.RedisOAuth2AuthorizationConsentService;
import com.acgist.oauth2.service.RedisOAuth2AuthorizationService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * Oauth2授权配置
 * 
 * @author acgist
 */
@Configuration
@EnableConfigurationProperties(Oauth2Config.class)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServerConfig.class);
	
	@Value("${system.jwk.path:}")
	private String jwkPath;
	@Value("${system.jwk.secret:}")
	private String jwkSecret;
	
	@Autowired
	private Oauth2Config oauth2Config;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity security) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(security);
		security.formLogin();
		return security.build();
	}
	
	@Bean
	public RedisOAuth2AuthorizationService redisOAuth2AuthorizationService() {
		return new RedisOAuth2AuthorizationService();
	}
	
	@Bean
	public RedisOAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService() {
		return new RedisOAuth2AuthorizationConsentService();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
		// 注意ID不能随机生成否者重启之后Redis不能正确加载
		final RegisteredClient clientWeb = RegisteredClient.withId(Oauth2Config.CLIENT_WEB)
			.clientId(Oauth2Config.CLIENT_WEB)
			.clientSecret(passwordEncoder.encode(this.oauth2Config.getWeb()))
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.redirectUri(this.oauth2Config.getRedirectUri())
			.tokenSettings(
				TokenSettings.builder()
					.accessTokenTimeToLive(Duration.ofSeconds(this.oauth2Config.getAccess()))
					.refreshTokenTimeToLive(Duration.ofSeconds(this.oauth2Config.getRefresh()))
					.build()
			)
			.scope("all")
			.build();
		final RegisteredClient clientRest = RegisteredClient.withId(Oauth2Config.CLIENT_REST)
			.clientId(Oauth2Config.CLIENT_REST)
			.clientSecret(passwordEncoder.encode(this.oauth2Config.getRest()))
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.redirectUri(this.oauth2Config.getRedirectUri())
			.tokenSettings(
				TokenSettings.builder()
					.accessTokenTimeToLive(Duration.ofSeconds(this.oauth2Config.getAccess()))
					.refreshTokenTimeToLive(Duration.ofSeconds(this.oauth2Config.getRefresh()))
					.build()
			)
			.scope("all")
			.build();
		return new InMemoryRegisteredClientRepository(clientWeb, clientRest);
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
	public JwtDecoder jwtDecoder(KeyPair keyPair) {
		return NimbusJwtDecoder
			.withPublicKey((RSAPublicKey) keyPair.getPublic())
			.build();
	}

}
