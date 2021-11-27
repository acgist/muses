package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayCode;

/**
 * <p>Interceptor - 验签</p>
 */
@Component
public class SignatureInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		if(session.verifySignature()) {
			return true;
		}
		session.buildFail(GatewayCode.CODE_1003).response(response);
		return false;
	}
	
}
