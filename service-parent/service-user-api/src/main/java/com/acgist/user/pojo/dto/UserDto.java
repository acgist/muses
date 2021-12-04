package com.acgist.user.pojo.dto;

import com.acgist.boot.PojoCopy;

public class UserDto extends PojoCopy {

	private static final long serialVersionUID = 1L;

	private String name;
	private String memo;
	
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

}
