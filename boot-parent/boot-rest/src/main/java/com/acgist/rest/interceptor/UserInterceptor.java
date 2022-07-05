package com.acgist.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.MessageCodeException;
import com.acgist.boot.model.User;
import com.acgist.boot.utils.JSONUtils;
import com.acgist.rest.UserContext;
import com.acgist.www.interceptor.AdapterInterceptor;

/**
 * 用户拦截
 * 
 * @author acgist
 */
public class UserInterceptor extends AdapterInterceptor {

	/**
	 * 忽略用户登陆
	 */
	@Value("${system.rest.ignore-user:true}")
	private boolean ignoreUser;
	
	@Override
	public String[] patterns() {
		return new String[] { "/**" };
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(this.error(request)) {
			return true;
		}
		final String currentUser = request.getHeader(User.HEADER_CURRENT_USER);
		if(StringUtils.isEmpty(currentUser)) {
			if(this.ignoreUser) {
				return true;
			}
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
