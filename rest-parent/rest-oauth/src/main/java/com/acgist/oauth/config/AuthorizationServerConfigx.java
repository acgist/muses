//package com.acgist.oauth.config;
//
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.util.UUID;
//
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Role;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
//import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//
///**
// * 认证服务
// * 
// * @author acgist
// */
//@Configuration
//@Import(OAuth2AuthorizationServerConfiguration.class)
//public class AuthorizationServerConfigx {
//	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	@Order(Ordered.HIGHEST_PRECEDENCE)
//	public SecurityFilterChain standardSecurityFilterChain(HttpSecurity http) throws Exception {
////		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//		http
//			.csrf().disable();
////			.formLogin(x -> {
////				x.usernameParameter("username");
////				x.passwordParameter("password");
////				x.loginPage("/login");
////				x.permitAll();
////			})
//		return http.build();
//	}
//	
//	@Bean
//	public SecurityFilterChain xxSecurityFilterChain(HttpSecurity http) throws Exception {
//		http
//		.csrf().disable()
//		.authorizeRequests()
//		.antMatchers("/oauth2/**").permitAll()
//		.anyRequest().authenticated()
//		.and()
//		.userDetailsService(this.userDetailsService())
//		.httpBasic();
////			.formLogin(x -> {
////				x.usernameParameter("username");
////				x.passwordParameter("password");
////				x.loginPage("/login");
////				x.permitAll();
////			})
//		return http.build();
//	}
//
//	@Bean
//	public RegisteredClientRepository registeredClientRepository() {
//		RegisteredClient loginClient = RegisteredClient.withId(UUID.randomUUID().toString())
//				.clientId("web-client")
//				.clientSecret(this.passwordEncoder().encode("123456"))
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.redirectUri("http://www.acgist.com")
//				.scope("all")
//				.build();
//		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//				.clientId("rest-client")
//				.clientSecret(this.passwordEncoder().encode("123456"))
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.scope("all")
//				.build();
//		// @formatter:on
//		return new InMemoryRegisteredClientRepository(loginClient, registeredClient);
//	}
//	
////	@Bean
////	public OAuth2AuthorizationService authorizationService() {
////		return new InMemoryOAuth2AuthorizationService();
////	}
//	
////	@Bean
////	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
////		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
////	}
////
////	@Bean
////	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
////		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
////	}
//
//
//	@Bean
//	public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
//		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//		// @formatter:off
//		RSAKey rsaKey = new RSAKey.Builder(publicKey)
//				.privateKey(privateKey)
//				.keyID(UUID.randomUUID().toString())
//				.build();
//		// @formatter:on
//		JWKSet jwkSet = new JWKSet(rsaKey);
//		return new ImmutableJWKSet<>(jwkSet);
//	}
//
//	@Bean
//	public JwtDecoder jwtDecoder(KeyPair keyPair) {
//		return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
//	}
//
//	@Bean
//	public ProviderSettings providerSettings() {
//		return ProviderSettings.builder().issuer("http://localhost:9000").build();
//	}
//
//	@Bean
//	public UserDetailsService userDetailsService() {
//		// @formatter:off
//		UserDetails userDetails = User.builder()
//				.username("user")
//				.password(this.passwordEncoder().encode("123456"))
//				.roles("user")
//				.build();
//		// @formatter:on
//
//		return new InMemoryUserDetailsManager(userDetails);
//	}
//
//	@Bean
//	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//	KeyPair generateRsaKey() {
//		KeyPair keyPair;
//		try {
//			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//			keyPairGenerator.initialize(2048);
//			keyPair = keyPairGenerator.generateKeyPair();
//		}
//		catch (Exception ex) {
//			throw new IllegalStateException(ex);
//		}
//		return keyPair;
//	}
//	
//}
