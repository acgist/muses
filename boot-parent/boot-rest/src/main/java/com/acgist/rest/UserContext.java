package com.acgist.rest;

import com.acgist.boot.pojo.bean.User;

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
