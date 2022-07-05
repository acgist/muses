package com.acgist.oauth2.service;

import com.acgist.oauth2.model.IPCountSession;

/**
 * IP请求次数统计
 * 
 * @author acgist
 */
public interface IPCountService {

	/**
	 * 获取请求次数
	 * 
	 * @param clientIP 客户端的IP
	 * 
	 * @return 请求次数
	 */
	IPCountSession get(String clientIP);

	/**
	 * 删除请求次数
	 * 
	 * @param clientIP 客户端的IP
	 */
	void remove(String clientIP);
	
}
