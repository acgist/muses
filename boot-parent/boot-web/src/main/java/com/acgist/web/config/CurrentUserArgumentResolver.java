package com.acgist.web.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.acgist.web.UserContext;
import com.acgist.www.config.WwwMethodArgumentResolver;

/**
 * 获取当前用户
 * 
 * @author acgist
 */
public class CurrentUserArgumentResolver implements WwwMethodArgumentResolver {
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return UserContext.currentUser();
	}

}
