package com.acgist.gateway.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * 删除
 * 
 * @author acgist
 */
@Getter
@Setter
public class DeleteRequest extends GatewayRequest {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 删除用户ID
	 */
	@NotNull(message = "删除用户ID不能为空")
	private Long id;
	
}
