package com.acgist.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acgist.boot.JSONUtils;
import com.acgist.boot.StringUtils;
import com.acgist.boot.data.MessageCode;
import com.acgist.boot.data.MessageCodeException;
import com.acgist.boot.data.User;
import com.acgist.rest.UserContext;
import com.acgist.www.ErrorUtils;
import com.acgist.www.interceptor.WwwInterceptor;

/**
 * 用户拦截
 * 
 * @author acgist
 */
public class UserInterceptor implements WwwInterceptor {

	@Override
	public String[] patterns() {
		return new String[] { "/**" };
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(ErrorUtils.error(request)) {
			return true;
		}
		final String currentUser = request.getHeader(User.HEADER_CURRENT_USER);
		if(StringUtils.isEmpty(currentUser)) {
			throw MessageCodeException.of(MessageCode.CODE_3401);
		}
		final User user = JSONUtils.toJava(currentUser, User.class);
		if(user == null) {
			throw MessageCodeException.of(MessageCode.CODE_3401);
		}
		UserContext.set(user);
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserContext.remove();
	}
	
}
