package com.acgist.gateway;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.acgist.boot.JSONUtils;
import com.acgist.boot.Message;

/**
 * 抽象网关
 * 
 * @author acgist
 */
public abstract class Gateway implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 请求地址
	 */
	public static final String GATEWAY = "/rest/**";
	/**
	 * 状态编码
	 */
	public static final String GATEWAY_CODE = "code";
	/**
	 * 状态信息
	 */
	public static final String GATEWAY_MESSAGE = "message";
	/**
	 * 请求标识
	 */
	public static final String GATEWAY_QUERY_ID = "queryId";
	/**
	 * 请求签名
	 */
	public static final String GATEWAY_SIGNATURE = "signature";
	/**
	 * 响应时间
	 */
	public static final String GATEWAY_RESPONSE_TIME = "responseTime";

	/**
	 * 签名数据
	 */
	@NotBlank(message = "签名内容不能为空")
	protected String signature;
	/**
	 * 透传信息
	 */
	@Size(max = 512, message = "透传信息长度不能超过512")
	protected String reserved;

	public String getSignature() {
		return signature;
	}

	public String getReserved() {
		return reserved;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

}
