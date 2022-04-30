package com.acgist.boot.model;

import lombok.Getter;
import lombok.Setter;

/**
 * KeyValueDto
 * 
 * @author acgist
 */
@Getter
@Setter
public class KeyValueVo<V> extends BootDto {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 键
	 */
	private String key;
	/**
	 * 值
	 */
	private V Value;
	
}
