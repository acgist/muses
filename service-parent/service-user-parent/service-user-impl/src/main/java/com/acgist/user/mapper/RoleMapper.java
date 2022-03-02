package com.acgist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.user.pojo.entity.RoleEntity;

/**
 * 角色Mapper
 * 
 * @author acgist
 */
@Mapper
public interface RoleMapper extends BootMapper<RoleEntity> {

	@Select("""
		SELECT role.* FROM t_role role
			LEFT JOIN t_user_role ur ON role.id = ur.role_id
			LEFT JOIN t_user user ON ur.user_id = user.id
		WHERE user.name = #{name}
		""")
	List<RoleEntity> findByUser(String name);
	
}
