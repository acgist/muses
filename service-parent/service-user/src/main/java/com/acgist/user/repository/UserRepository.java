package com.acgist.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.acgist.user.pojo.dto.UserDto;
import com.acgist.user.pojo.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<UserEntity, Long> {
    
    @Query(value = "select name, memo from t_user where name = :name limit 1", nativeQuery = true)
    UserDto findDtoByName(String name);
    
    @Query(value = "select name, memo from t_user", nativeQuery = true)
    List<UserDto> findDtoList();

	UserEntity findByName(String name);
	
}
