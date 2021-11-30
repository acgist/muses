package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.common.MessageCode;
import com.acgist.common.StringUtils;
import com.acgist.rest.GatewaySession;

/**
 * 数据格式校验
 * 
 * @author acgist
 */
@Component
public class ValidatorInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final String message = session.validator();
		if (StringUtils.isNotEmpty(message)) {
			session.buildFail(MessageCode.CODE_1002, message).response(response);
			return false;
		}
		return true;
	}

}
