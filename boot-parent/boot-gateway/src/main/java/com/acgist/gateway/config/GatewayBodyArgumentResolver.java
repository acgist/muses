package com.acgist.gateway.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.acgist.gateway.GatewaySession;
import com.acgist.www.config.WwwMethodArgumentResolver;

/**
 * 获取请求数据
 * 
 * @author acgist
 */
public class GatewayBodyArgumentResolver implements WwwMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(GatewayBody.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return GatewaySession.getInstance().getGatewayRequest();
	}

}
