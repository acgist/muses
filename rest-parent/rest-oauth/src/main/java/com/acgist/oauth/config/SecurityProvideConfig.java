package com.acgist.oauth.config;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import com.acgist.oauth.service.UserService;

@Configuration
public class SecurityProvideConfig {

	@Value("${system.jwt:acgist}")
	private String jwt;
	@Value("${system.secret.jwt:acgist}")
	private String secret;

	@Autowired
	private UserService userService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return authentication -> {
			final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
			daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
			daoAuthenticationProvider.setUserDetailsService(this.userService);
			daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
			return daoAuthenticationProvider.authenticate(authentication);
		};
	}

	@Bean
	public KeyPair jwtKeyPair() {
		final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(this.jwt), this.secret.toCharArray());
		return keyStoreKeyFactory.getKeyPair("jwt");
	}

}