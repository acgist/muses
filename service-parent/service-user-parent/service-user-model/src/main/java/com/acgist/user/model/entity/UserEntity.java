package com.acgist.user.model.entity;

import com.acgist.model.entity.StateEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName(value = "t_user")
public class UserEntity extends StateEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 密码
	 */
	@TableField(value = "password")
	private String password;
	
}
