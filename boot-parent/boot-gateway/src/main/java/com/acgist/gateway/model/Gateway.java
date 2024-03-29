package com.acgist.gateway.model;

import javax.validation.constraints.Size;

import com.acgist.boot.model.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * 抽象网关
 * 
 * @author acgist
 */
@Getter
@Setter
public abstract class Gateway extends Model {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 透传信息
	 */
	@Size(max = 512, message = "透传信息长度不能超过512")
	protected String reserved;

}
