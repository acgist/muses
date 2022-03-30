package com.acgist.model.query;

import java.util.Collection;

import org.elasticsearch.core.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.model.entity.NameEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

public class FilterQueryTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterQueryTest.class);
	
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
		LOGGER.info("语句：{}", build.getTargetSql());
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
		LOGGER.info("单值：{}", array);
	}
	
	public void log(Object ... array) {
		LOGGER.info("数组：{}", array);
	}
	
	public void log(Collection<?> collection) {
		LOGGER.info("集合：{}", collection);
	}
	
}
