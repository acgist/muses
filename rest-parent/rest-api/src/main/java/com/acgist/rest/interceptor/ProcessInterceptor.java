package com.acgist.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.rest.GatewaySession;
import com.acgist.rest.config.GatewayCode;
import com.acgist.rest.notice.NoticeService;
import com.acgist.rest.service.GatewayService;
import com.acgist.rest.service.UniqueIdService;

/**
 * <p>Interceptor - 正在处理拦截</p>
 * 
 * @author acgist
 */
@Component
public class ProcessInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private UniqueIdService uniqueIdService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final String queryId = this.uniqueIdService.id();
		if(session.buildProcess(queryId)) {
			return true;
		}
		session.buildFail(GatewayCode.CODE_1001).response(response);
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		if(session.record()) {
			this.noticeService.put(session);
			this.gatewayService.record(session);
		}
		session.completeProcess();
	}

}
