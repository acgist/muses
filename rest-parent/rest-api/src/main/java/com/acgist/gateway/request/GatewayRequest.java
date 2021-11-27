package com.acgist.gateway.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.acgist.gateway.Executor;
import com.acgist.gateway.Gateway;

/**
 * <p>抽象请求</p>
 * 
 * @param <T> 请求执行器
 */
public abstract class GatewayRequest<T extends Executor> extends Gateway {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>请求执行器类型</p>
	 */
	private final Class<T> executorClass;
	/**
	 * <p>请求时间</p>
	 */
	@Pattern(regexp = "\\d{14}", message = "请求时间格式错误")
	@NotBlank(message = "请求时间不能为空")
	protected String requestTime;
	
	/**
	 * @param executorClass 请求执行器类型
	 */
	protected GatewayRequest(Class<T> executorClass) {
		this.executorClass = executorClass;
	}

	public Class<T> getExecutorClass() {
		return executorClass;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

}
