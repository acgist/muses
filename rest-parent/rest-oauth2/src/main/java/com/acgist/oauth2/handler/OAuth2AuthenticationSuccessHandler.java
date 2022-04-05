package com.acgist.oauth2.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.acgist.oauth2.config.LoginType;

import lombok.extern.slf4j.Slf4j;

/**
 * 登陆成功处理器
 * 
 * @author acgist
 */
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	private FailCountManager failCountManager;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
		log.debug("登陆成功：{}", username);
		this.failCountManager.remove(username);
		if(LoginType.get().isHtml()) {
			super.onAuthenticationSuccess(request, response, authentication);
		} else {
			response.sendRedirect("/oauth2/authorize?" + authentication.getDetails().toString());
		}
	}
	
}
