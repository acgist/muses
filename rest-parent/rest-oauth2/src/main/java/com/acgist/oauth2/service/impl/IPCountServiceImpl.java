package com.acgist.oauth2.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acgist.oauth2.model.IPCountSession;
import com.acgist.oauth2.service.IPCountService;

public class IPCountServiceImpl implements IPCountService {

	/**
	 * 请求次数
	 */
	private Map<String, IPCountSession> map = new ConcurrentHashMap<>();

	@Override
	public IPCountSession get(String clientIP) {
		if(clientIP == null) {
			return new IPCountSession();
		}
		return this.map.computeIfAbsent(clientIP, key -> new IPCountSession());
	}

	@Override
	public void remove(String clientIP) {
		if(clientIP != null) {
			this.map.remove(clientIP);
		}
	}
	
}
