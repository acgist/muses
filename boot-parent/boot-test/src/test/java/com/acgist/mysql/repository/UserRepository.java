package com.acgist.mysql.repository;

import org.springframework.stereotype.Repository;

import com.acgist.dao.repository.BootRepository;
import com.acgist.user.pojo.entity.UserEntity;

@Repository
public interface UserRepository extends BootRepository<UserEntity> {

}
