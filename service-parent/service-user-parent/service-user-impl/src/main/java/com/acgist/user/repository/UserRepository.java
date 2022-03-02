package com.acgist.user.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.acgist.dao.repository.BootRepository;
import com.acgist.data.query.TemplateQuery;
import com.acgist.user.pojo.dto.UserDto;
import com.acgist.user.pojo.entity.UserEntity;
import com.acgist.user.pojo.query.UserQuery;

/**
 * 用户查询
 * 
 * @author acgist
 */
@Repository
public interface UserRepository extends BootRepository<UserEntity> {

	UserEntity findByName(String name);
	
	@TemplateQuery(
		query = "insert into t_user(id, name, password, create_date, modify_date) values (:id, :name, :password, now(), now())",
		nativeQuery = true
	)
	default void insert(UserEntity userEntity) {
	}
	
	@TemplateQuery(
		query = "update t_user set name = :name, modify_date = now() where id = :id"
	)
	default void update(UserEntity userEntity) {
	}
	
	@TemplateQuery(
		query = "delete from t_user where id = :id"
	)
	default void delete(Long id) {
	}
	
	@TemplateQuery(
		query = "select name, memo from t_user where id is null",
		fallback = true
	)
	default UserDto fallback() {
		final UserDto userDto = new UserDto();
		userDto.setName("fallback");
		userDto.setMemo("fallback");
		return userDto;
	}
	
	@TemplateQuery(
		query = "select name, memo, (select count(*) from t_user) size from t_user",
		where = "$(name != null && name == root) and name = :name",
		sorted = "order by id desc",
		attach = "limit 1"
	)
	default UserDto query(String name) {
		return null;
	}
	
	@TemplateQuery(
		query = "select name, memo, (select count(*) from t_user) size from t_user",
		where = """
			$(name != null) and name = :name
			$(beginDate != null && endDate == null) and create_date > :beginDate
			$(beginDate == null && endDate != null) and create_date < :endDate
			$(beginDate != null && endDate != null) and create_date between :beginDate and :endDate
		""",
		sorted = "order by id desc",
		attach = "limit 1"
	)
	default UserDto query(UserQuery userQuery) {
		return null;
	}
	
	@TemplateQuery(
		query = "select name, memo, (select count(*) from t_user) size from t_user",
		where = """
			$(name != null) and name = :name
			$(beginDate != null && endDate == null) and create_date > :beginDate
			$(beginDate == null && endDate != null) and create_date < :endDate
			$(beginDate != null && endDate != null) and create_date between :beginDate and :endDate
		""",
		sorted = "order by id desc",
		attach = "limit 1"
	)
	default UserDto query(Map<String, Object> queryMap) {
		return null;
	}
	
	@TemplateQuery(
		query = "select name, memo, (select count(*) from t_user) size from t_user",
		where = """
			$(name != null) and name = :name
			$(bool) and name = :name
			$(beginDate != null && endDate == null) and create_date > :beginDate
			$(beginDate == null && endDate != null) and create_date < :endDate
			$(beginDate != null && endDate != null) and create_date between :beginDate and :endDate
		""",
		sorted = "order by id desc",
		attach = "limit 1"
	)
	default UserDto query(String name, boolean bool, Date beginDate, Date endDate) {
		return null;
	}
	
	@TemplateQuery(
		query = "select name, memo from t_user",
		resultType = UserDto.class
	)
	default List<UserDto> queryList() {
		return null;
	}
	
	@TemplateQuery(
		query = "select name, memo from t_user",
		count = "select count(*) from t_user",
		resultType = UserDto.class
	)
	default Page<UserDto> queryList(Pageable pageable) {
		return null;
	}
	
	@TemplateQuery(
		query = "select name, memo from t_user",
		count = "select count(*) from t_user",
		where = """
			$(name != null) and name = :name
			$(beginDate != null && endDate == null) and create_date > :beginDate
			$(beginDate == null && endDate != null) and create_date < :endDate
			$(beginDate != null && endDate != null) and create_date between :beginDate and :endDate
		""",
		sorted = "order by create_date desc",
		resultType = UserDto.class
	)
	default Page<UserDto> queryList(UserQuery userQuery, Pageable pageable) {
		return null;
	}
	
	@TemplateQuery(
		query = "SELECT user.name as name, user.memo as memo FROM UserEntity user",
		where = "$(name != null && name == root) and user.name = :name",
		nativeQuery = false
	)
	default UserDto queryJpql(String name) {
		return null;
	}
	
}
