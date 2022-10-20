package com.acgist.model.query;

import java.util.Collection;

import org.elasticsearch.core.List;
import org.junit.jupiter.api.Test;

import com.acgist.model.entity.NameEntity;
import com.acgist.model.query.FilterQuery.Filter;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterQueryTest {

	@TableName(value = "tb_user")
	public static class UserEntity extends NameEntity {
		private static final long serialVersionUID = 1L;
	}

	@Test
	public void testWrapper() {
		final FilterQuery query = FilterQuery.builder()
			.eq("name", "eq")
			.like("name", "like")
			.in("name", List.of("in", "in"))
			.notIn("name", List.of("notIn", "notIn"))
			.include("name", List.of("notIn", "notIn"))
			.exclude("name", List.of("notIn", "notIn"))
			.isNull("name")
			.desc("id")
			.asc("name");
		final Wrapper<UserEntity> build = query.build(UserEntity.class);
		log.info("语句：{}", build.getTargetSql());
	}

	@Test
	public void testRemove() {
		final FilterQuery query = FilterQuery.builder()
			.eq("name", "eq");
		final Filter name = query.remove("name");
		final QueryWrapper<UserEntity> buildWrapper = (QueryWrapper<UserEntity>) query.build(UserEntity.class);
		buildWrapper.and(wrapper -> {
			Filter.Type.LIKE.of("name-a", name.getValue()).filter(UserEntity.class, wrapper.or());
			Filter.Type.LIKE.of("name-b", name.getValue()).filter(UserEntity.class, wrapper.or());
		});
		log.info("语句：{}", buildWrapper.getTargetSql());
	}
	
	@Test
	public void testAlias() {
		final FilterQuery query = FilterQuery.builder()
			.eq("name", "eq");
		query.getFilter().add(new Filter("other", "age", 12, Filter.Type.EQ));
		final Wrapper<UserEntity> build = query.build("user", UserEntity.class);
		log.info("语句：{}", build.getTargetSql());
		log.info("语句：{}", build.getCustomSqlSegment());
	}
	
	@Test
	public void testOrder() {
		final FilterQuery query = FilterQuery.builder()
			.eq("name", "eq");
		query.getFilter().add(new Filter("other", "age", 12, Filter.Type.EQ));
//		query.desc("age");
		query.desc("age").sortPage();
		final Wrapper<UserEntity> build = query.build("user", UserEntity.class);
		log.info("语句：{}", build.getTargetSql());
		log.info("语句：{}", build.getCustomSqlSegment());
	}
	
	@Test
	public void testType() {
		final Object array = new Object[] {1, 2};
		this.log(array);
		this.log(new Object[] {1, 2});
		final Object list = List.of(1, 2);
		this.log(list);
		this.log(List.of(1, 2));
	}
	
	public void log(Object array) {
		log.info("单值：{}", array);
	}
	
	public void log(Object ... array) {
		log.info("数组：{}", array);
	}
	
	public void log(Collection<?> collection) {
		log.info("集合：{}", collection);
	}
	
}
