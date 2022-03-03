package com.acgist.gateway.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.acgist.gateway.Gateway;

import lombok.Getter;
import lombok.Setter;

/**
 * 网关请求
 * 
 * @author acgist
 */
@Getter
@Setter
public abstract class GatewayRequest extends Gateway {

	private static final long serialVersionUID = 1L;

	/**
	 * 请求时间
	 */
	@Pattern(regexp = "\\d{14}", message = "请求时间格式错误")
	@NotBlank(message = "请求时间不能为空")
	protected String reqTime;

}
