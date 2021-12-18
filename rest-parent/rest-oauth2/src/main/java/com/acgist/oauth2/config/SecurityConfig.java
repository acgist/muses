package com.acgist.oauth2.config;
//package com.acgist.oauth.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.acgist.oauth.service.UserService;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
////	@Autowired
////	private AuthenticationManager authenticationManager;
//
//	@Override
//	protected void configure(HttpSecurity security) throws Exception {
//		security
//			.csrf().disable()
//			.authorizeRequests()
////			.authorizeRequests().antMatchers("/oauth2/**").permitAll()
//			.anyRequest().authenticated()
//			.and()
//			// 指定页面：.formLogin()
//			.httpBasic();
//	}
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
//		builder
//			.userDetailsService(this.userService)
//			.passwordEncoder(this.passwordEncoder);
//	}
//
//}
