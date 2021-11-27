package com.acgist.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.acgist.web.service.UserService;

/**
 * 安全配置
 * 
 * @author acgist
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * {@inheritDoc}
	 * 
	 * 不要使用转发successForwardUrl/failureForwardUrl（错误状态：405）
	 * 
	 * 如果需要细化鉴定权限：.access("@roleService.hasPermission(request, authentication)")
	 * 
	 * @author acgist
	 */
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
			// 单条规则
//			.antMatcher("/user/**").authorizeRequests().anyRequest().authenticated()
			// 多条规则
			.authorizeRequests().antMatchers("/user/**").authenticated()
			.anyRequest().permitAll()
			.and()
			// 失败页面
			.exceptionHandling().accessDeniedPage("/login")
			.and()
			// 登出配置
			.logout().logoutUrl("/logout").logoutSuccessUrl("/")
			.and()
			// 登陆配置
			.formLogin().usernameParameter("username").passwordParameter("password").loginPage("/login")
			.loginProcessingUrl("/login").defaultSuccessUrl("/user").failureUrl("/login");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(this.userService).passwordEncoder(this.passwordEncoder);
	}

}
