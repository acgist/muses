package com.acgist.user.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色
 * 
 * @author acgist
 */
@Getter
@Setter
public class RoleDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色权限
	 */
	private List<String> paths;
	
}
