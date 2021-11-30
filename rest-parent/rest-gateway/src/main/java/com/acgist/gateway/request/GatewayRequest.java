package com.acgist.gateway.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.acgist.rest.Gateway;

/**
 * 抽象请求
 * 
 * @param <T> 请求执行器
 */
public abstract class GatewayRequest extends Gateway {

	private static final long serialVersionUID = 1L;

	/**
	 * 请求时间
	 */
	@Pattern(regexp = "\\d{14}", message = "请求时间格式错误")
	@NotBlank(message = "请求时间不能为空")
	protected String requestTime;

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

}
