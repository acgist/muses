package com.acgist.oauth2.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.acgist.oauth2.service.IPCountService;
import com.acgist.www.utils.WebUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 登陆成功处理器
 * 
 * @author acgist
 */
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	private IPCountService ipCountService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		final String clientIP = WebUtils.clientIP(request);
		log.debug("登陆成功：{}", clientIP);
		this.ipCountService.remove(clientIP);
		if(WebUtils.responseHTML(request)) {
			super.onAuthenticationSuccess(request, response, authentication);
		} else {
//			request.getRequestDispatcher("/oauth2/authorize").forward(request, response);
			response.sendRedirect("/oauth2/authorize?" + authentication.getDetails().toString());
		}
	}
	
}
