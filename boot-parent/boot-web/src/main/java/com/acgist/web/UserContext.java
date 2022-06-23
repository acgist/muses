package com.acgist.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.acgist.web.model.WebUser;

/**
 * 用户上下文
 * 
 * @author acgist
 */
public class UserContext {

	/**
	 * 异步线程
	 */
	private static final InheritableThreadLocal<WebUser> ASYNC_LOCAL = new InheritableThreadLocal<>();
	
	/**
	 * 设置异步线程当前用户
	 */
	public static final void set() {
		final WebUser currentUser = currentUser();
		if(currentUser != null) {
			ASYNC_LOCAL.set(currentUser);
		}
	}
	
	/**
	 * 获取当前用户
	 * 
	 * @return 当前用户
	 */
	public static final WebUser currentUser() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication.isAuthenticated()) {
			return (WebUser) authentication.getPrincipal();
		}
		return ASYNC_LOCAL.get();
	}
	
	/**
	 * 移除当前用户
	 */
	public static final void remove() {
		ASYNC_LOCAL.remove();
	}
	
}
