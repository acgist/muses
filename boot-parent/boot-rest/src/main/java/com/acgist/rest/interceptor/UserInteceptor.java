package com.acgist.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.boot.MessageCode;
import com.acgist.boot.MessageCodeException;
import com.acgist.boot.StringUtils;
import com.acgist.boot.pojo.bean.User;
import com.acgist.rest.UserContext;
import com.acgist.user.service.IUserService;

public class UserInteceptor implements HandlerInterceptor {

	@DubboReference
	private IUserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String name = request.getHeader(User.HEADER_NAME);
		if(StringUtils.isEmpty(name)) {
			throw new MessageCodeException(MessageCode.CODE_1004);
		}
		final User user = this.userService.findByName(name);
		if(user == null) {
			throw new MessageCodeException(MessageCode.CODE_1004);
		}
		UserContext.set(user);
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserContext.remove();
	}
	
}
