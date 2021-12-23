package com.acgist.user.pojo.dto;

import java.math.BigInteger;

import com.acgist.boot.PojoCopy;

public class UserDto extends PojoCopy {

	private static final long serialVersionUID = 1L;

	private String name;
	private String memo;
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
