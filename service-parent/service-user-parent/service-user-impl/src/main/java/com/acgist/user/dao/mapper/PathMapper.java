package com.acgist.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.user.data.entity.PathEntity;

/**
 * 权限Mapper
 * 
 * @author acgist
 */
@Mapper
public interface PathMapper extends BootMapper<PathEntity> {

	/**
	 * 根据用户名称查询权限列表
	 * 
	 * @param name 用户名称
	 * 
	 * @return 权限列表
	 */
	List<PathEntity> findByUser(String name);
	
}
