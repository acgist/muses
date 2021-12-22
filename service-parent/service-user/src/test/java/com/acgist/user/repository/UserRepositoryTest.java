package com.acgist.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.acgist.data.query.FilterQuery;
import com.acgist.main.UserApplication;
import com.acgist.user.pojo.dto.UserDto;
import com.acgist.user.pojo.entity.UserEntity;

@SpringBootTest(classes = UserApplication.class)
public class UserRepositoryTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryTest.class);
	
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
	
	@Test
	public void testQueryByName() {
		final UserDto dto = this.userRepository.queryByName("root");
		LOGGER.info("{}", dto);
		assertNotNull(dto);
	}
	
	@Test
	public void getQueryList() {
		final List<UserDto> list = this.userRepository.queryList();
		LOGGER.info("{}", list);
		assertNotNull(list);
	}
	
	@Test
	public void testFindPage() {
		final Pageable pageable = PageRequest.of(0, 1);
		Page<UserEntity> page = this.userRepository.findAll(pageable);
		assertEquals(2, page.getTotalPages());
		assertEquals(2L, page.getTotalElements());
		assertEquals(1, page.getContent().size());
		final UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		page = this.userRepository.findAll(Example.of(userEntity), pageable);
		assertEquals(1, page.getTotalPages());
		assertEquals(1L, page.getTotalElements());
		assertEquals(1, page.getContent().size());
		final ExampleMatcher matcher = ExampleMatcher.matching()
			.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
		userEntity.setId(null);
		userEntity.setName("t");
		page = this.userRepository.findAll(Example.of(userEntity, matcher), pageable);
		assertEquals(2, page.getTotalPages());
		assertEquals(2L, page.getTotalElements());
		assertEquals(1, page.getContent().size());
	}
	
	@Test
	public void testFindDate() {
		List<UserEntity> list = this.userRepository.findAll(new Specification<UserEntity>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Calendar instance = Calendar.getInstance();
				instance.set(Calendar.YEAR, 2000);
				return query.where(criteriaBuilder.between(root.get("createDate").as(Date.class), instance.getTime(), new Date())).getRestriction();
			}
		});
		assertEquals(2, list.size());
		list = this.userRepository.findAll(new Specification<UserEntity>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return query.where(criteriaBuilder.between(root.get("createDate").as(Date.class), new Date(), new Date())).getRestriction();
			}
		});
		assertEquals(0, list.size());
	}
	
	@Test
	public void testFindCondition() {
		List<UserEntity> list = this.userRepository.findAll(FilterQuery.<UserEntity>builder().eq("id", 1).build());
		assertEquals(1, list.size());
		list = this.userRepository.findAll(FilterQuery.<UserEntity>builder().like("name", "%t%").desc("id").build());
		assertEquals(2, list.size());
//		Optional<UserEntity> optional = this.userRepository.findOne(FilterQuery.<UserEntity>builder().like("name", "%t%").build());
//		assertTrue(optional.isPresent());
		Page<UserEntity> page = this.userRepository.findAll(FilterQuery.<UserEntity>builder().like("name", "%t%").desc("id").build(), PageRequest.of(0, 1));
		assertEquals(1, page.getContent().size());
		LOGGER.info("{}", page.getContent());
		this.userRepository.findAll(FilterQuery.<UserEntity>builder().like("name", "%t%").asc("id").build(), PageRequest.of(0, 1));
		assertEquals(1, page.getContent().size());
		LOGGER.info("{}", page.getContent());
	}
	
}
