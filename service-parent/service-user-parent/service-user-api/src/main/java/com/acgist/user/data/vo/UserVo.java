package com.acgist.user.data.vo;

import com.acgist.boot.data.BootVo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserVo extends BootVo {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	private String name;

}
