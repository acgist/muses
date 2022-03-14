package com.acgist.user.model.dto;

import java.math.BigInteger;

import com.acgist.boot.model.BootDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BootDto {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String memo;
	/**
	 * 默认返回类型可以直接使用Number类型接收
	 */
	private BigInteger size;

}
