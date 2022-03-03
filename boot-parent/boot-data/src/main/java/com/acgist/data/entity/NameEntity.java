package com.acgist.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据库实体类
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class NameEntity extends BootEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_MEMO = "memo";
	
	/**
	 * 名称
	 */
	@TableField(value = "name")
	private String name;
	/**
	 * 描述
	 */
	@TableField(value = "memo")
	private String memo;

}
