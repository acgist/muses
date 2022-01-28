package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import com.acgist.boot.JSONUtils;
import com.acgist.boot.StringUtils;
import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.pojo.MessageCode;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayMapping;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.service.GatewayMappingService;
import com.acgist.www.interceptor.WwwInterceptor;

/**
 * 数据打包
 * 
 * @author acgist
 */
public class PackageInterceptor implements WwwInterceptor {

	@Autowired
	private GatewayMappingService gatewayMappingService;

	@Override
	public int order() {
		return 200;
	}
	
	@Override
	public String[] patterns() {
		return new String[] { "/**" };
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance();
		final GatewayMapping gatewayMapping = this.gatewayMappingService.gatewayMapping(request.getMethod(), request.getRequestURI());
		if(gatewayMapping == null) {
			session.buildFail(MessageCode.CODE_1000);
			session.fail(response);
			return false;
		}
		session.setGatewayMapping(gatewayMapping);
		final String requestData = StreamUtils.copyToString(request.getInputStream(), MusesConfig.CHARSET);
		if(StringUtils.isEmpty(requestData)) {
			session.buildFail(MessageCode.CODE_1002);
			session.fail(response);
			return false;
		}
		session.setRequestData(requestData);
		final GatewayRequest gatewayRequest = JSONUtils.toJava(requestData, gatewayMapping.getClazz());
		if(gatewayRequest == null) {
			session.buildFail(MessageCode.CODE_1002);
			session.fail(response);
			return false;
		}
		session.setGatewayRequest(gatewayRequest);
		return true;
	}
	
}
