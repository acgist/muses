package com.acgist.gateway.model;

import java.io.Serializable;

import javax.validation.constraints.Size;

import com.acgist.boot.JSONUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 抽象网关
 * 
 * @author acgist
 */
@Getter
@Setter
public abstract class Gateway implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 透传信息
	 */
	@Size(max = 512, message = "透传信息长度不能超过512")
	protected String reserved;

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

}
