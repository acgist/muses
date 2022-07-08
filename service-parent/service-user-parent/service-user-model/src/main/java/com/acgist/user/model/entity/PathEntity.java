package com.acgist.user.model.entity;

import java.util.List;

import com.acgist.model.entity.StateEntity;
import com.acgist.user.model.type.PathType;
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

	/**
	 * 匹配规则
	 * 
	 * GET:/user/id
	 * POST:/user
	 * DELETE：/user/delete
	 */
	@TableField(value = "path")
	private String path;
	/**
	 * 权限类型：API-接口；MENU-菜单；BUTTON-按钮；
	 */
	private PathType type;
	/**
	 * 前端路由
	 */
	private String route;
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
