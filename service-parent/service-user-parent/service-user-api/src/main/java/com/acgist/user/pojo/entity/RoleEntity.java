package com.acgist.user.pojo.entity;

import com.acgist.data.pojo.entity.StateEntity;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_role")
public class RoleEntity extends StateEntity {

	private static final long serialVersionUID = 1L;
	
}
