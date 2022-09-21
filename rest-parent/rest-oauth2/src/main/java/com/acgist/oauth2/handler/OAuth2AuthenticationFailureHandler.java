package com.acgist.oauth2.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.acgist.boot.model.Message;
import com.acgist.boot.utils.ErrorUtils;
import com.acgist.oauth2.model.IPCountSession;
import com.acgist.oauth2.service.IPCountService;
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
	private IPCountService ipCountService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		final String clientIP = WebUtils.clientIP(request);
		final IPCountSession failCountSession = this.ipCountService.get(clientIP);
		final long failCount = failCountSession.increment();
		log.info("登陆失败：{}-{}", clientIP, failCount);
		ResponseUtils.response(Message.fail(ErrorUtils.messageCode(401, exception), exception.getMessage()), response);
	}
	
}
