package com.acgist.mysql.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.user.pojo.entity.UserEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Mapper
public interface UserMapper extends BootMapper<UserEntity> {

	Page<UserEntity> page(Page<UserEntity> page);
	
}
