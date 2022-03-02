package com.acgist.user.pojo.entity;

import com.acgist.data.pojo.entity.StateEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_path")
public class PathEntity extends StateEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PATH = "path";
	public static final String PROPERTY_PARENT = "parent";
	public static final String PROPERTY_CHILDREN = "children";

	/**
	 * 匹配规则
	 * 
	 * GET:/user/name
	 * POST:/user/name
	 */
	@TableField(value = "path")
	private String path;
	/**
	 * 上级
	 */
	@TableField(value = "parent_id")
	private Long parentId;

}
