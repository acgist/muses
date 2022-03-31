package com.acgist.user.model.entity;

import java.util.List;

import com.acgist.model.entity.StateEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 权限
 * 
 * @author acgist
 */
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
	/**
	 * 上级ID路径（方便删除）：/1/2
	 */
	@TableField(value = "parent_id_path")
	private String parentIdPath;
	/**
	 * 下级菜单
	 */
	private List<PathEntity> children;

}
