package com.acgist.user.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.acgist.data.query.FilterQuery;
import com.acgist.user.pojo.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<UserEntity, Long> {

	UserEntity findByName(String name);
	
	@Query(
		nativeQuery = true,
		value = "select * from t_user user where 1 = 1 "
			+ "and if(:name != '', user.name = :name, 1 = 1) "
			+ "and if(IFNULL(	:begin))",
		countQuery = @FilterQuery(build = "")
	)
	Page<UserEntity> findByCondition(String name, Date begin, Date end, Pageable pageable);
	
}
