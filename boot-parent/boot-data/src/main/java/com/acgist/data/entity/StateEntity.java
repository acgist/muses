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
public abstract class StateEntity extends NameEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_SORTED = "sorted";
	public static final String PROPERTY_ENABLE = "enable";
	
	/**
	 * 排序
	 */
	@TableField(value = "sorted")
	private Integer sorted;
	/**
	 * 是否可用
	 */
	@TableField(value = "enable")
	private Boolean enable;

}
