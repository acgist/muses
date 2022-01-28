package com.acgist.mysql.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.mysql.MySQLApplication;
import com.acgist.user.pojo.entity.UserEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@SpringBootTest(classes = MySQLApplication.class)
public class MyBatisTest {
	
	@Autowired
	private UserMapper userMapper;

	@Test
	public void testQuery() {
		final Page<UserEntity> page = this.userMapper.selectPage(new Page<UserEntity>(0, 10), new LambdaQueryWrapper<UserEntity>());
		assertNotNull(page);
		assertFalse(page.getRecords().isEmpty());
	}
	
	@Test
	public void testPage() {
		final Page<UserEntity> page = this.userMapper.page(new Page<UserEntity>(0, 1));
		assertNotNull(page);
		assertEquals(1, page.getRecords().size());
		assertEquals(2, page.getTotal());
	}
	
}
