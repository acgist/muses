package com.acgist.model.query;

import java.util.Collection;

import org.elasticsearch.core.List;
import org.junit.jupiter.api.Test;

import com.acgist.model.entity.NameEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterQueryTest {

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
		final Wrapper<NameEntity> build = query.build(NameEntity.class);
		log.info("语句：{}", build.getTargetSql());
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
