package com.acgist.rest.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.acgist.boot.model.User;
import com.acgist.rest.UserContext;
import com.acgist.www.resolver.CurrentUser;
import com.acgist.www.resolver.CurrentUser.Type;
import com.acgist.www.resolver.WwwMethodArgumentResolver;

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
		final User currentUser = UserContext.currentUser();
		if(currentUser == null) {
			return null;
		}
		final Type value = parameter.getParameterAnnotation(CurrentUser.class).value();
		if(value == Type.ID) {
			return currentUser.getId();
		} else if(value == Type.NAME) {
			return currentUser.getName();
		} else if(value == Type.USER) {
			return currentUser;
		}
		final Class<?> type = parameter.getParameterType();
		if(Long.class.equals(type)) {
			return currentUser.getId();
		} else if(String.class.equals(type)) {
			return currentUser.getName();
		} else {
			return currentUser;
		}
	}

}
