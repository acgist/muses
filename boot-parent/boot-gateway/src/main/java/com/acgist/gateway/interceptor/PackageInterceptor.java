package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.boot.JSONUtils;
import com.acgist.boot.StringUtils;
import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.pojo.bean.MessageCode;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayMapping;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.service.GatewayMappingService;

/**
 * 数据打包
 * 
 * @author acgist
 */
public class PackageInterceptor implements HandlerInterceptor {

	@Autowired
	private GatewayMappingService gatewayMappingService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance();
		final GatewayMapping gatewayMapping = this.gatewayMappingService.gatewayMapping(request.getMethod(), request.getRequestURI());
		if(gatewayMapping == null) {
			session.buildFail(MessageCode.CODE_1000);
			session.response(response);
			return false;
		}
		session.setGatewayMapping(gatewayMapping);
		final String requestJSON = StreamUtils.copyToString(request.getInputStream(), MusesConfig.CHARSET);
		if(StringUtils.isEmpty(requestJSON)) {
			session.buildFail(MessageCode.CODE_1002);
			session.response(response);
			return false;
		}
		session.setRequestJSON(requestJSON);
		final GatewayRequest gatewayRequest = JSONUtils.toJava(requestJSON, gatewayMapping.getClazz());
		if(gatewayRequest == null) {
			session.buildFail(MessageCode.CODE_1002);
			session.response(response);
			return false;
		}
		session.setGatewayRequest(gatewayRequest);
		return true;
	}
	
}
