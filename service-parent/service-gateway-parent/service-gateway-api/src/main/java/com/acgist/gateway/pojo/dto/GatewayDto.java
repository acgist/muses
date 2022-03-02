package com.acgist.gateway.pojo.dto;

import com.acgist.boot.pojo.BootDto;

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

	private Long queryId;
	private String request;
	private String response;

}
