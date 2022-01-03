package com.acgist.gateway.interceptor;

import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.boot.pojo.bean.MessageCode;
import com.acgist.boot.service.IdService;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.pojo.dto.GatewayDto;
import com.acgist.www.ErrorUtils;

/**
 * 处理拦截
 * 
 * @author acgist
 */
public class ProcessInterceptor implements HandlerInterceptor {

	@Autowired
	private IdService idService;
	@Autowired
	Consumer<GatewayDto> gatewayPush;
	@Autowired
	private ApplicationContext context;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final Long queryId = this.idService.id();
		if (session.buildProcess(queryId)) {
			return true;
		}
		// 注意不要完成
		session.buildFail(MessageCode.CODE_1001);
		session.fail(response);
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final GatewaySession session = GatewaySession.getInstance();
		if (session.record()) {
			final GatewayDto gatewayDto = new GatewayDto();
			gatewayDto.setQueryId(session.getQueryId());
			gatewayDto.setRequest(session.getRequestData());
			if(session.hasResponse()) {
				gatewayDto.setResponse(session.getResponseJSON());
			} else {
				final Object errorMessage = request.getAttribute(ErrorUtils.ERROR_MESSAGE);
				if(errorMessage != null) {
					gatewayDto.setResponse(errorMessage.toString());
				}
			}
			this.gatewayPush.accept(gatewayDto);
		}
		session.completeProcess();
	}

}
