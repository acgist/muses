package com.acgist.gateway.interceptor;

import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.service.IdService;
import com.acgist.gateway.model.GatewaySession;
import com.acgist.notify.gateway.model.dto.GatewayDto;
import com.acgist.www.interceptor.WwwInterceptor;
import com.acgist.www.utils.ErrorUtils;

/**
 * 处理拦截
 * 
 * @author acgist
 */
public class ProcessInterceptor implements WwwInterceptor {

	@Autowired
	private IdService idService;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private Consumer<GatewayDto> gatewayPush;

	@Override
	public int order() {
		return -100;
	}
	
	@Override
	public String[] patterns() {
		return new String[] { "/**" };
	}
	
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
				final Object errorMessage = ErrorUtils.getSystemErrorMessage(request);
				if(errorMessage != null) {
					gatewayDto.setResponse(errorMessage.toString());
				}
			}
			this.gatewayPush.accept(gatewayDto);
		}
		session.completeProcess();
	}

}
