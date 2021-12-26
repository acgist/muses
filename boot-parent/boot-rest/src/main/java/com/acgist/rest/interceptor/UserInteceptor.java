package com.acgist.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.boot.JSONUtils;
import com.acgist.boot.MessageCodeException;
import com.acgist.boot.StringUtils;
import com.acgist.boot.pojo.bean.MessageCode;
import com.acgist.boot.pojo.bean.User;
import com.acgist.rest.UserContext;

public class UserInteceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String currentUser = request.getHeader(User.HEADER_CURRENT_USER);
		if(StringUtils.isEmpty(currentUser)) {
			throw new MessageCodeException(MessageCode.CODE_3401);
		}
		final User user = JSONUtils.toJava(currentUser, User.class);
		if(user == null) {
			throw new MessageCodeException(MessageCode.CODE_3401);
		}
		UserContext.set(user);
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserContext.remove();
	}
	
}
