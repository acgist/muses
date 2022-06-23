package com.acgist.rest;

import com.acgist.boot.model.User;

/**
 * 用户上下文
 * 
 * 通过网关透传用户
 * 
 * 注意：没有权限信息
 * 
 * @author acgist
 */
public class UserContext {

	/**
	 * 线程信息
	 */
	private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();
	/**
	 * 异步线程
	 */
	private static final InheritableThreadLocal<User> ASYNC_LOCAL = new InheritableThreadLocal<>();
	
	/**
	 * 设置当前用户
	 * 
	 * @param user 当前用户
	 */
	public static final void set(User user) {
		LOCAL.set(user);
		ASYNC_LOCAL.set(user);
	}
	
	/**
	 * 获取当前用户
	 * 
	 * @return 当前用户
	 */
	public static final User currentUser() {
		final User user = LOCAL.get();
		if(user != null) {
			return user;
		}
		return ASYNC_LOCAL.get();
	}
	
	/**
	 * 移除当前用户
	 */
	public static final void remove() {
		LOCAL.remove();
		ASYNC_LOCAL.remove();
	}
	
}
