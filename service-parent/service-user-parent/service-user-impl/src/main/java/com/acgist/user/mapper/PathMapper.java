package com.acgist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.user.pojo.entity.PathEntity;

/**
 * 权限Mapper
 * 
 * @author acgist
 */
@Mapper
public interface PathMapper extends BootMapper<PathEntity> {

	@Select("""
		SELECT path.* FROM t_path path
			LEFT JOIN t_role_path rp ON path.id = rp.role_id
			LEFT JOIN t_role role ON rp.role_id = role.id
			LEFT JOIN t_user_role ur ON role.id = ur.role_id
			LEFT JOIN t_user user ON ur.user_id = user.id
		WHERE user.name = #{name}
		""")
	List<PathEntity> findByUser(String name);
	
}
