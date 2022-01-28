package com.acgist.gateway.pojo.dto;

import com.acgist.boot.pojo.BootDto;

/**
 * 网关DTO
 * 
 * @author acgist
 */
public class GatewayDto extends BootDto {

	private static final long serialVersionUID = 1L;

	private Long queryId;
	private String request;
	private String response;

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
