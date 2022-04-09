package com.acgist.mysql.mapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.boot.JSONUtils;
import com.acgist.model.query.FilterQuery;
import com.acgist.mysql.MySQLApplication;
import com.acgist.user.model.entity.UserEntity;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@SpringBootTest(classes = MySQLApplication.class)
public class MyBatisTest {
	
	@Autowired
	private UserMapper userMapper;

	@Test
	public void testQuery() {
		final Page<UserEntity> page = this.userMapper.selectPage(new Page<UserEntity>(0, 10), Wrappers.lambdaQuery());
		assertNotNull(page);
		assertFalse(page.getRecords().isEmpty());
		assertDoesNotThrow(() -> JSONUtils.toJSON(page));
	}
	
	@Test
	public void testPage() {
		final Page<UserEntity> page = this.userMapper.page(new Page<UserEntity>(0, 1));
		assertNotNull(page);
		assertEquals(1, page.getRecords().size());
		assertEquals(2, page.getTotal());
	}
	
	@Test
	public void testFilterQuery() {
		 final FilterQuery query = FilterQuery.builder();
		 assertEquals(1, this.userMapper.selectList(query.build(UserEntity.class)).size());
		 assertEquals(1, this.userMapper.selectList(query.reset().eq("name", "root").build(UserEntity.class)).size());
		 assertEquals(0, this.userMapper.selectList(query.reset().eq("name", "acgist").build(UserEntity.class)).size());
		 assertEquals(1, this.userMapper.selectList(query.reset().like("name", "o").build(UserEntity.class)).size());
	}
	
}
