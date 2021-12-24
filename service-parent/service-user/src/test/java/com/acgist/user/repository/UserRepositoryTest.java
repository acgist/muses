package com.acgist.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.transaction.annotation.Transactional;

import com.acgist.boot.CostUtils;
import com.acgist.main.UserApplication;
import com.acgist.user.pojo.dto.UserDto;
import com.acgist.user.pojo.entity.UserEntity;
import com.acgist.user.pojo.query.UserQuery;

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
//	@Rollback(false)
	@Transactional
	public void testInsert() {
		final UserEntity entity = new UserEntity();
		entity.setId(3L);
		entity.setName("insert");
		entity.setPassword(new BCryptPasswordEncoder().encode("123456"));
		this.userRepository.insert(entity);
	}
	
	@Test
	@Transactional
	public void testUpdate() {
		final UserEntity entity = new UserEntity();
		entity.setId(1L);
		entity.setName("root");
		this.userRepository.update(entity);
	}
	
	@Test
	@Transactional
	public void testDelete() {
		this.userRepository.delete(3L);
	}

	@Test
	public void testFallback() {
		final UserDto userDto = this.userRepository.fallback();
		assertNotNull(userDto);
		LOGGER.info("{}", userDto);
	}
	
	@Test
	public void testCosted() {
		CostUtils.costed(10000, () -> {
			this.userRepository.findByName("root");
		});
		CostUtils.costed(10000, () -> {
			this.userRepository.query("root");
		});
	}
	
	@Test
	public void testQuery() {
		UserDto dto = this.userRepository.query("root");
		LOGGER.info("{}", dto);
		assertNotNull(dto);
		assertEquals("root", dto.getName());
		dto = this.userRepository.query((String) null);
		LOGGER.info("{}", dto);
		assertNotNull(dto);
		assertEquals("acgist", dto.getName());
		final UserQuery userQuery = new UserQuery();
		this.userRepository.query(userQuery);
		userQuery.setName("root");
		this.userRepository.query(userQuery);
		userQuery.setBeginDate(new Date());
		this.userRepository.query(userQuery);
		userQuery.setBeginDate(null);
		userQuery.setEndDate(new Date());
		this.userRepository.query(userQuery);
		userQuery.setBeginDate(new Date());
		userQuery.setEndDate(new Date());
		this.userRepository.query(userQuery);
		this.userRepository.query(null, null, null);
		this.userRepository.query("root", null, null);
		this.userRepository.query(null, null, new Date());
		this.userRepository.query(null, new Date(), null);
		this.userRepository.query(null, new Date(), new Date());
		final Map<String, Object> map = new HashMap<>();
		this.userRepository.query(map);
		map.put("name", "acgist");
		this.userRepository.query(map);
	}
	
	@Test
	public void getQueryList() {
		List<UserDto> list = this.userRepository.queryList();
		LOGGER.info("{}", list);
		assertNotNull(list);
		Page<UserDto> page = this.userRepository.queryList(PageRequest.of(0, 1));
		LOGGER.info("{}", page);
		LOGGER.info("{}", page.getContent());
		assertNotNull(page);
		final UserQuery userQuery = new UserQuery();
		userQuery.setBeginDate(new Date());
		userQuery.setName("root");
		userQuery.setEndDate(new Date());
		page = this.userRepository.queryList(userQuery, PageRequest.of(0, 1));
		LOGGER.info("{}", page);
		LOGGER.info("{}", page.getContent());
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
		final ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
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
	
}
