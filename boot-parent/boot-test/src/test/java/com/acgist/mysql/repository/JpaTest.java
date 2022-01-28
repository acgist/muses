package com.acgist.mysql.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.mysql.MySQLApplication;
import com.acgist.user.pojo.entity.UserEntity;

@SpringBootTest(classes = MySQLApplication.class)
public class JpaTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testQuery() {
		final List<UserEntity> list = this.userRepository.findAll();
		assertNotNull(list);
		assertFalse(list.isEmpty());
	}
	
}
