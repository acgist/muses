package com.acgist.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.user.data.entity.RoleEntity;

/**
 * 角色Mapper
 * 
 * @author acgist
 */
@Mapper
public interface RoleMapper extends BootMapper<RoleEntity> {

	/**
	 * 根据用户名称查询角色列表
	 * 
	 * @param name 用户名称
	 * 
	 * @return 角色列表
	 */
	List<RoleEntity> findByUser(String name);
	
}
