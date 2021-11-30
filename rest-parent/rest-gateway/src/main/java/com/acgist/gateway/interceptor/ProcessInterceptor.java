package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.common.MessageCode;
import com.acgist.common.service.IdService;
import com.acgist.rest.GatewaySession;

/**
 * 正在处理拦截
 * 
 * @author acgist
 */
@Component
public class ProcessInterceptor implements HandlerInterceptor {

	@Autowired
	private IdService idService;
	@Autowired
	private ApplicationContext context;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final Long queryId = this.idService.id();
		if (session.buildProcess(queryId)) {
			return true;
		}
		session.buildFail(MessageCode.CODE_1001).response(response);
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
		throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		if (session.record()) {
			// TODO：MQ：保存推送
		}
		session.completeProcess();
	}

}
