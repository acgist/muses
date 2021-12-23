package com.acgist.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.acgist.data.query.TemplateQuery;
import com.acgist.user.pojo.dto.UserDto;
import com.acgist.user.pojo.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    
    @TemplateQuery(
    	select = "select name, memo from t_user",
    	where = "name = :name",
    	attach = "limit 1",
    	nativeQuery = true
    )
    default UserDto queryByName(String name) {
    	return null;
    }
    
    @TemplateQuery(
    	select = "select name, memo from t_user",
    	clazz = UserDto.class,
    	nativeQuery = true
	)
    default List<UserDto> queryList() {
    	return null;
    }

	UserEntity findByName(String name);
	
}
