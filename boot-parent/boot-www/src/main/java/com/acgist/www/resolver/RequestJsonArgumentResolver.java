package com.acgist.www.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.acgist.boot.utils.JSONUtils;

/**
 * 获取当前请求参数
 * 
 * 区别`@RequestBody`
 * 
 * @author acgist
 */
public class RequestJsonArgumentResolver implements WwwMethodArgumentResolver {
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestJson.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		final String name = parameter.getParameterAnnotation(RequestJson.class).name();
		final String json = webRequest.getParameter(name);
		if(StringUtils.isEmpty(json)) {
			return null;
		}
		return JSONUtils.toJava(json, parameter.getParameterType());
	}

}
