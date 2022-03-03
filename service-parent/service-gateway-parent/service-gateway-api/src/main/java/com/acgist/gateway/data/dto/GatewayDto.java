package com.acgist.gateway.data.dto;

import com.acgist.boot.data.BootDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 网关DTO
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class GatewayDto extends BootDto {

	private static final long serialVersionUID = 1L;

	/**
	 * QueryId
	 */
	private Long queryId;
	/**
	 * 请求报文
	 */
	private String request;
	/**
	 * 响应报文
	 */
	private String response;

}
