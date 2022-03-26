package com.acgist.model.query;

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
	
}
