package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.boot.MessageCode;
import com.acgist.boot.service.IdService;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.pojo.dto.GatewayDto;
import com.acgist.gateway.service.IGatewayService;

/**
 * 处理拦截
 * 
 * @author acgist
 */
public class ProcessInterceptor implements HandlerInterceptor {

	@Autowired
	private IdService idService;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private IGatewayService gatewayService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println(request.getMethod());
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final Long queryId = this.idService.id();
		if (session.buildProcess(queryId)) {
			return true;
		}
		session.buildFail(MessageCode.CODE_1001).response(response);
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		if (session.record()) {
			final GatewayDto gatewayDto = new GatewayDto();
			gatewayDto.setQueryId(session.getQueryId());
			gatewayDto.setRequest(session.getRequestJSON());
			gatewayDto.setResponse(session.getResponseJSON());
			this.gatewayService.push(gatewayDto);
		}
		session.completeProcess();
	}

}
