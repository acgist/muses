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
@TableName(value = "t_user")
public class UserEntity extends StateEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_ROLES = "roles";
	
	/**
	 * 密码
	 */
	@TableField(value = "password")
	private String password;
	
}
