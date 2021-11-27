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
	 * 如果需要鉴定权限：.access("@roleService.hasPermission(request, authentication)")
	 * 
	 * @author acgist
	 */
	@Order(1)
	@Configurable
	@EnableWebSecurity
	public class UserConfig extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity security) throws Exception {
			security
				// CSRF
				.csrf().disable()
				// 框架
				.headers().frameOptions().sameOrigin()
				// HTTPS
				.httpStrictTransportSecurity().disable()
				.and()
				// 配置单条规则
//				.antMatcher("/user/**").authorizeRequests().anyRequest().authenticated()
				// 配置多条规则
				.authorizeRequests().antMatchers("/user/**").authenticated()
				.anyRequest().permitAll()
				.and()
				// 失败
				.exceptionHandling().accessDeniedPage("/login")
				.and()
				// 登出
				.logout().logoutUrl("/logout").logoutSuccessUrl("/")
				.and()
				// 登陆
				.formLogin().usernameParameter("username").passwordParameter("password")
				.loginPage("/login").loginProcessingUrl("/login")
				.defaultSuccessUrl("/user").failureUrl("/login");
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder builder) throws Exception {
			builder
				.userDetailsService(SecurityConfig.this.userService)
				.passwordEncoder(SecurityConfig.this.passwordEncoder);
		}
		
	}
	
}
