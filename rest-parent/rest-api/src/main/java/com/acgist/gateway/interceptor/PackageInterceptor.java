package com.acgist.gateway.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.Gateway;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.config.GatewayMapping;
import com.acgist.gateway.config.GatewayMappingConfig;
import com.acgist.gateway.service.GatewayService;
import com.acgist.utils.JSONUtils;

/**
 * <p>Interceptor - 数据打包</p>
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
		final String json = this.json(request);
		if(StringUtils.isEmpty(json)) {
			session.buildFail(GatewayCode.CODE_1002).response(response);
			return false;
		}
		final Map<String, Object> requestData = JSONUtils.toMap(json);
		if(requestData == null) {
			session.buildFail(GatewayCode.CODE_1002).response(response);
			return false;
		}
		session.setRequestData(requestData);
		final GatewayMapping gatewayMapping = this.gatewayMappingConfig.gatewayMapping(requestData.get(GatewayService.GATEWAY_GATEWAY));
		if(gatewayMapping == null) {
			session.buildFail(GatewayCode.CODE_1000).response(response);
			return false;
		}
		session.setGateway(gatewayMapping);
		final Gateway gateway = JSONUtils.unserialize(json, gatewayMapping.getRequestClass());
		if(gateway == null) {
			session.buildFail(GatewayCode.CODE_1002).response(response);
			return false;
		}
		session.setRequest(gateway);
		return true;
	}

	/**
	 * <p>读取JSON请求数据</p>
	 * 
	 * @param request 请求
	 * 
	 * @return JSON请求数据
	 * 
	 * @throws IOException IO异常
	 */
	private String json(HttpServletRequest request) throws IOException {
		return StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
	}
	
}
