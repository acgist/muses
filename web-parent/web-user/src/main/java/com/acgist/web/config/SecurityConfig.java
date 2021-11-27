package com.acgist.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.acgist.web.service.UserService;

/**
 * 安全
 * 
 * @author acgist
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 用户中心拦截
	 * 
	 * 不要使用转发successForwardUrl和failureForwardUrl：405
	 * 
	 * @author acgist
	 */
	@Order(1)
	@Configurable
	public class UserConfig extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity security) throws Exception {
			security
				.csrf().disable()
				.antMatcher("/user/**").authorizeRequests().anyRequest().access("@roleService.hasPermission(request, authentication)")
				.and()
				.exceptionHandling().accessDeniedPage("/login")
				.and()
				.logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
				.and()
				.formLogin().usernameParameter("username").passwordParameter("password")
				.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/user").failureUrl("/login")
				.permitAll();
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder builder) throws Exception {
			builder
				.userDetailsService(SecurityConfig.this.userService)
				.passwordEncoder(SecurityConfig.this.passwordEncoder);
		}
		
	}
	
	/**
	 * 允许其他所有请求
	 * 
	 * @author acgist
	 */
	@Order(2)
	@Configurable
	public class AllConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity security) throws Exception {
			security
				.csrf().disable()
				// 允许框架
				.headers().frameOptions().sameOrigin()
				// HTTPS
				.httpStrictTransportSecurity().disable()
				.and()
				.antMatcher("/**").authorizeRequests().anyRequest().permitAll();
		}
	}
	
}
