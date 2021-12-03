package com.acgist.gateway;

import java.io.Serializable;

import javax.validation.constraints.Size;

import com.acgist.boot.JSONUtils;

/**
 * 抽象网关
 * 
 * 需要添加字段校验
 * 
 * 请求数据需要加密 响应数据需要签名
 * 
 * @author acgist
 */
public abstract class Gateway implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_QUERY_ID = "queryId";
	public static final String PROPERTY_RESERVED = "reserved";
	public static final String PROPERTY_RESP_TIME = "respTime";
	
	/**
	 * 透传信息
	 */
	@Size(max = 512, message = "透传信息长度不能超过512")
	protected String reserved;

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

}
