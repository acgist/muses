package com.acgist.gateway.request;

import javax.validation.constraints.NotNull;

/**
 * 删除
 * 
 * @author acgist
 */
public class DeleteRequest extends GatewayRequest {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 删除用户ID
	 */
	@NotNull(message = "删除用户ID不能为空")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
