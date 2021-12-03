package com.acgist.pay.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.acgist.main.UserApplication;
import com.acgist.user.pojo.entity.UserEntity;
import com.acgist.user.repository.UserRepository;

@SpringBootTest(classes = UserApplication.class)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void testSave() {
		final UserEntity entity = new UserEntity();
		entity.setName("acgist");
		entity.setPassword(new BCryptPasswordEncoder().encode("123456"));
		entity.setMemo("测试");
		this.userRepository.save(entity);
	}
	
}
