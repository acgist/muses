package com.acgist.user.api;

import java.util.List;

import com.acgist.user.model.dto.RoleDto;

/**
 * 角色
 * 
 * @author acgist
 */
public interface IRoleService {

	/**
	 * 查询所有角色和权限
	 * 
	 * @return 所有角色和权限
	 */
	List<RoleDto> all();
	
}
