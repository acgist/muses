package com.acgist.gateway.request;

import javax.validation.constraints.NotNull;

/**
 * 
 * @author acgist
 */
public class SetNameRequest extends GatewayRequest {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户名称
	 */
	@NotNull(message = "用户名称不能为空")
	private String name;
	/**
	 * 通知地址
	 */
	private String notice;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}
