package com.acgist.boot.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.user.pojo.entity.UserEntity;

@Mapper
public interface UserMapper extends BootMapper<UserEntity> {

}
