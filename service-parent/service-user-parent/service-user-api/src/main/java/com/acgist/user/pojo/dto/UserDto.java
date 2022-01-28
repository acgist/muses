package com.acgist.user.pojo.dto;

import java.math.BigInteger;

import com.acgist.data.dto.BootDto;

public class UserDto extends BootDto {

	private static final long serialVersionUID = 1L;

	private String name;
	private String memo;
	/**
	 * 默认返回类型可以直接使用Number类型接收
	 */
	private BigInteger size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigInteger getSize() {
		return size;
	}

	public void setSize(BigInteger size) {
		this.size = size;
	}

}
