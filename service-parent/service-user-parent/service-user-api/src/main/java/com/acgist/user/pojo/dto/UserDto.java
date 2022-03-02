package com.acgist.user.pojo.dto;

import java.math.BigInteger;

import com.acgist.boot.pojo.BootDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BootDto {

	private static final long serialVersionUID = 1L;

	private String name;
	private String memo;
	/**
	 * 默认返回类型可以直接使用Number类型接收
	 */
	private BigInteger size;

}
