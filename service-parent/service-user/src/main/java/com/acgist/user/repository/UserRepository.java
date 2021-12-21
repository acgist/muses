package com.acgist.user.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.acgist.user.pojo.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<UserEntity, Long> {

	UserEntity findByName(String name);
	
}
