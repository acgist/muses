package com.acgist.gateway.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.acgist.gateway.Gateway;

/**
 * 网关请求
 * 
 * @author acgist
 */
public abstract class GatewayRequest extends Gateway {

	private static final long serialVersionUID = 1L;

	/**
	 * 请求时间
	 */
	@Pattern(regexp = "\\d{14}", message = "请求时间格式错误")
	@NotBlank(message = "请求时间不能为空")
	protected String reqTime;

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

}
