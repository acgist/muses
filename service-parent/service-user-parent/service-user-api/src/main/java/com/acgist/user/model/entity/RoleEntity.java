package com.acgist.user.model.entity;

import com.acgist.model.entity.StateEntity;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_role")
public class RoleEntity extends StateEntity {

	private static final long serialVersionUID = 1L;
	
}
