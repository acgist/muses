package com.acgist.oauth2.service;

import com.acgist.oauth2.model.FailCountSession;

/**
 * 登陆失败次数管理
 * 
 * @author acgist
 */
public interface FailCountService {

	/**
	 * 获取失败次数
	 * 
	 * @param clientIP 客户端的IP
	 * 
	 * @return 失败次数
	 */
	FailCountSession get(String clientIP);

	/**
	 * 登陆成功删除失败次数
	 * 
	 * @param clientIP 客户端的IP
	 */
	void remove(String clientIP);
	
}
