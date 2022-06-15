package com.acgist.user.model.vo;

import com.acgist.boot.model.EntityVo;

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
public class UserVo extends EntityVo {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	private String name;

}
