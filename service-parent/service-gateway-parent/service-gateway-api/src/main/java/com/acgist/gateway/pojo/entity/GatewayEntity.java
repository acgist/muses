package com.acgist.gateway.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.acgist.data.pojo.entity.BootEntity;

@Entity
@Table(name = "t_gateway", indexes = {
	@Index(name = "index_gateway_query_id", columnList = "query_id", unique = true)
})
public class GatewayEntity extends BootEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * QueryId
	 */
	@Column(name = "query_id")
	private Long queryId;
	/**
	 * 请求
	 */
	@Column(length = 1024, nullable = false)
	private String request;
	/**
	 * 响应
	 */
	@Column(length = 1024)
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
