package com.acgist.rest;

import com.acgist.boot.pojo.bean.User;

/**
 * 网关透传用户
 * 
 * 注意：没有权限信息
 * 
 * @author acgist
 */
public class UserContext {

	private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();
	
	public static final void set(User user) {
		LOCAL.set(user);
	}
	
	public static final User currentUser() {
		return LOCAL.get();
	}
	
	public static final void remove() {
		LOCAL.remove();
	}
	
}
