package com.acgist.rest;

import com.acgist.boot.pojo.bean.User;

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
	 * 设置当前用户
	 * 
	 * @param user 当前用户
	 */
	public static final void set(User user) {
		LOCAL.set(user);
	}
	
	/**
	 * 获取当前用户
	 * 
	 * @return 当前用户
	 */
	public static final User currentUser() {
		return LOCAL.get();
	}
	
	/**
	 * 移除当前用户
	 */
	public static final void remove() {
		LOCAL.remove();
	}
	
}
