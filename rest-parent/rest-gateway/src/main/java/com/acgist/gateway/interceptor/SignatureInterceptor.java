package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.boot.MessageCode;
import com.acgist.rest.GatewaySession;

/**
 * 验签
 * 
 * @author acgist
 */
@Component
public class SignatureInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		if (session.verifySignature()) {
			return true;
		}
		session.buildFail(MessageCode.CODE_1003).response(response);
		return false;
	}

}