package com.acgist.gateway.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.boot.JSONUtils;
import com.acgist.boot.MessageCode;
import com.acgist.boot.StringUtils;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayMapping;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.service.GatewayMappingService;
import com.acgist.gateway.service.RsaService;

/**
 * 数据打包
 * 
 * @author acgist
 */
public class PackageInterceptor implements HandlerInterceptor {

	@Autowired
	private RsaService rsaService;
	@Autowired
	private GatewayMappingService gatewayMappingService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance();
		final String json = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
		if(StringUtils.isEmpty(json)) {
			session.buildFail(MessageCode.CODE_1002).response(response);
			return false;
		}
//		final String requestJSON = this.rsaService.decrypt(json);
		final String requestJSON = json;
		final Map<String, Object> requestData = JSONUtils.toMap(requestJSON);
		if(requestData == null) {
			session.buildFail(MessageCode.CODE_1002).response(response);
			return false;
		}
		session.setRequestJSON(requestJSON);
		final GatewayMapping gatewayMapping = this.gatewayMappingService.gatewayMapping(request.getMethod(), request.getRequestURI());
		if(gatewayMapping == null) {
			session.buildFail(MessageCode.CODE_1000).response(response);
			return false;
		}
		session.setGatewayMapping(gatewayMapping);
		final GatewayRequest gatewayRequest = JSONUtils.toJava(json, gatewayMapping.getClazz());
		if(gatewayRequest == null) {
			session.buildFail(MessageCode.CODE_1002).response(response);
			return false;
		}
		session.setGatewayRequest(gatewayRequest);
		return true;
	}
	
}
