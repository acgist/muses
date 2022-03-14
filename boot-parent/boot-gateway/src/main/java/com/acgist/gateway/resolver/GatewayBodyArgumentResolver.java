package com.acgist.gateway.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.acgist.gateway.model.GatewaySession;
import com.acgist.www.resolver.WwwMethodArgumentResolver;

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
