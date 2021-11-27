package com.acgist.rest.response;

import com.acgist.rest.Gateway;

/**
 * <p>抽象响应</p>
 * <p>不实用：直接维护GatewaySession.responseData</p>
 * 
 * @author acgist
 */
public abstract class GatewayResponse extends Gateway {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>请求标识</p>
	 */
	private String queryId;

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
}
