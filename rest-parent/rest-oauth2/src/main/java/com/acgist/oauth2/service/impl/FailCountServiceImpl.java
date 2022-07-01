package com.acgist.oauth2.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acgist.oauth2.model.FailCountSession;
import com.acgist.oauth2.service.FailCountService;

public class FailCountServiceImpl implements FailCountService {

	/**
	 * 失败次数
	 */
	private Map<String, FailCountSession> failCount = new ConcurrentHashMap<>();

	@Override
	public FailCountSession get(String clientIP) {
		if(clientIP == null) {
			return new FailCountSession();
		}
		return this.failCount.computeIfAbsent(clientIP, key -> new FailCountSession());
	}

	@Override
	public void remove(String clientIP) {
		if(clientIP != null) {
			this.failCount.remove(clientIP);
		}
	}
	
}
