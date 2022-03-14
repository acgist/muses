package com.acgist.user.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.user.model.entity.UserEntity;

/**
 * 用户Mapper
 * 
 * @author acgist
 */
@Mapper
public interface UserMapper extends BootMapper<UserEntity> {
	
	@Select("SELECT * FROM t_user WHERE name = #{name}")
	UserEntity findByName(String name);
	
}
