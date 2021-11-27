package com.acgist.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.rest.GatewaySession;
import com.acgist.rest.config.GatewayCode;

/**
 * <p>Interceptor - 数据格式校验</p>
 */
@Component
public class ValidatorInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final String message = session.validator();
		if(StringUtils.isNotEmpty(message)) {
			session.buildFail(GatewayCode.CODE_1002, message).response(response);
			return false;
		}
		return true;
	}
	
}
