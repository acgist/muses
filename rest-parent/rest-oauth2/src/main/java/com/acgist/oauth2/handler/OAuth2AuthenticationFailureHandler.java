package com.acgist.oauth2.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.acgist.boot.model.Message;
import com.acgist.boot.utils.UrlUtils;
import com.acgist.oauth2.config.LoginType;
import com.acgist.oauth2.model.FailCountSession;
import com.acgist.oauth2.service.FailCountService;
import com.acgist.www.utils.ResponseUtils;
import com.acgist.www.utils.WebUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 登陆失败处理器
 * 
 * @author acgist
 */
@Slf4j
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	@Autowired
	private FailCountService failCountService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		final String clientIP = WebUtils.clientIP(request);
		final FailCountSession failCountSession = this.failCountService.get(clientIP);
		final long failCount = failCountSession.fail();
		log.info("登陆失败：{}-{}", clientIP, failCount);
		if(LoginType.get().isHtml()) {
			response.sendRedirect("/oauth2/login?message=" + UrlUtils.encode(exception.getMessage()));
		} else {
			ResponseUtils.response(Message.fail(exception.getMessage()), response);
		}
	}
	
}
