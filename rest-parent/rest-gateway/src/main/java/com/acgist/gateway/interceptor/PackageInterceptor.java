package com.acgist.gateway.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.common.MessageCode;
import com.acgist.common.JSONUtils;
import com.acgist.common.StringUtils;
import com.acgist.rest.Gateway;
import com.acgist.rest.GatewaySession;
import com.acgist.rest.config.GatewayMapping;
import com.acgist.rest.config.GatewayMappingConfig;

/**
 * 数据打包
 * 
 * @author acgist
 */
@Component
public class PackageInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private GatewayMappingConfig gatewayMappingConfig;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final String json = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
		if(StringUtils.isEmpty(json)) {
			session.buildFail(MessageCode.CODE_1002).response(response);
			return false;
		}
		final Map<String, Object> requestData = JSONUtils.toMap(json);
		if(requestData == null) {
			session.buildFail(MessageCode.CODE_1002).response(response);
			return false;
		}
		session.setRequestData(requestData);
		final GatewayMapping gatewayMapping = this.gatewayMappingConfig.gatewayMapping(request.getRequestURI());
		if(gatewayMapping == null) {
			session.buildFail(MessageCode.CODE_1000).response(response);
			return false;
		}
		session.setGateway(gatewayMapping);
		final Gateway gateway = JSONUtils.toJava(json, gatewayMapping.getClazz());
		if(gateway == null) {
			session.buildFail(MessageCode.CODE_1002).response(response);
			return false;
		}
		session.setRequest(gateway);
		return true;
	}
	
}
