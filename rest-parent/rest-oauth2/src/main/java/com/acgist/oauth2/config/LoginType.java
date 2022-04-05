package com.acgist.oauth2.config;

import lombok.Getter;

/**
 * 登陆方式
 * 
 * @author acgist
 */
@Getter
public enum LoginType {

	/**
	 * 短信登陆
	 */
	SMS(true),
	/**
	 * 密码登陆
	 */
	PASSWORD(true),
	/**
	 * 密码认证登陆
	 */
	AUTHORIZE(false);
	
	/**
	 * 页面登陆
	 */
	private final boolean html;
	
	private LoginType(boolean html) {
		this.html = html;
	}

	/**
	 * 当前登陆方式
	 */
	private static final ThreadLocal<LoginType> LOCAL = new ThreadLocal<LoginType>();
	
	/**
	 * @return 当前登陆方式
	 */
	public static final LoginType get() {
		final LoginType loginType = LOCAL.get();
		return loginType == null ? PASSWORD : loginType;
	}
	
	/**
	 * 设置当前登陆方式
	 * 
	 * @param loginType 登陆方式
	 */
	public static final void set(LoginType loginType) {
		LOCAL.set(loginType);
	}
	
}
