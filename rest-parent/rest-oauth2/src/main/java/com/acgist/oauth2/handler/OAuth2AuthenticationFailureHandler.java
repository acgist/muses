package com.acgist.oauth2.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.acgist.boot.UrlUtils;
import com.acgist.boot.model.Message;
import com.acgist.oauth2.config.LoginType;
import com.acgist.www.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 登陆失败处理器
 * 
 * @author acgist
 */
@Slf4j
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	@Autowired
	private FailCountManager failCountManager;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		final String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
		final FailCountSession failCountSession = this.failCountManager.get(username);
		final long failCount = failCountSession.fail();
		log.info("登陆失败：{}-{}", username, failCount);
		if(LoginType.get().isHtml()) {
			response.sendRedirect("/login?message=" + UrlUtils.encode(exception.getMessage()));
		} else {
			ResponseUtils.response(Message.fail(exception.getMessage()), response);
		}
	}
	
}
