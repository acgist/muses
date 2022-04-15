package com.acgist.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户上下文
 * 
 * @author acgist
 */
public class UserContext {

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
		return null;
	}
	
}
