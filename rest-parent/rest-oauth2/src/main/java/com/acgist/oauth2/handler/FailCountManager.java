package com.acgist.oauth2.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登陆失败次数管理
 * 
 * @author acgist
 */
public class FailCountManager {

	/**
	 * 失败次数
	 */
	private Map<String, FailCountSession> failCount = new ConcurrentHashMap<>();

	/**
	 * 获取失败次数
	 * 
	 * @param username 用户名称
	 * 
	 * @return 失败次数
	 */
	public FailCountSession get(String username) {
		if(username == null) {
			return new FailCountSession();
		}
		return this.failCount.computeIfAbsent(username, key -> new FailCountSession());
	}

	/**
	 * 登陆成功删除失败次数
	 * 
	 * @param username 用户名称
	 */
	public void remove(String username) {
		if(username != null) {
			this.failCount.remove(username);
		}
	}
	
}
