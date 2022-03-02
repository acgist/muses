package com.acgist.user.pojo.vo;

import com.acgist.boot.pojo.BootVo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserVo extends BootVo {

	private static final long serialVersionUID = 1L;

	private String name;

}
