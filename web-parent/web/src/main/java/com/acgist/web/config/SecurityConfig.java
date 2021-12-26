package com.acgist.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置
 * 
 * @author acgist
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * {@inheritDoc}
	 * 
	 * 不要使用转发：successForwardUrl/failureForwardUrl（错误状态：405）
	 * 
	 * 如果需要鉴权：.access("@roleService.hasPermission(request, authentication)")
	 * 
	 * @author acgist
	 */
	@Override
	protected void configure(HttpSecurity security) throws Exception {
		security
			.csrf().disable()
			.headers().frameOptions().sameOrigin()
//			.httpStrictTransportSecurity().disable()
			.and()
			// 单条规则
//			.antMatcher("/user/**").authorizeRequests().anyRequest().authenticated()
			// 多条规则
			.authorizeRequests().antMatchers("/user/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.exceptionHandling()
			// 没有权限
			.accessDeniedPage("/login")
			// 没有认证
//			.authenticationEntryPoint(null)
			.and()
			.logout().logoutUrl("/logout").logoutSuccessUrl("/")
			.and()
			// 防止SessionFixation攻击
//			.sessionManagement().sessionFixation()
			.formLogin().usernameParameter("username").passwordParameter("password").loginPage("/login")
			.loginProcessingUrl("/login").defaultSuccessUrl("/user").failureUrl("/login");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);
	}

}
