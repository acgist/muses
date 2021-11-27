package com.acgist.gateway;

import java.util.Map;

/**
 * <p>请求执行器</p>
 * 
 * @author acgist
 */
public interface Executor {

	/**
	 * <p>执行请求返回响应</p>
	 * 
	 * @return 响应
	 */
	Map<String, Object> response();
	
}
