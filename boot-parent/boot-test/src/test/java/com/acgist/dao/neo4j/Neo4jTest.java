package com.acgist.dao.neo4j;

import org.elasticsearch.common.collect.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.dao.DaoApplication;

@SpringBootTest(classes = DaoApplication.class)
public class Neo4jTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jTest.class);

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void testSave() {
		final PersonNode xiaoming = new PersonNode();
		xiaoming.setId(1L);
		xiaoming.setAge((short) 20);
		xiaoming.setName("小明");
		final PersonNode xiaohong = new PersonNode();
		xiaohong.setId(2L);
		xiaohong.setAge((short) 20);
		xiaohong.setName("小红");
		final PersonNode xiaozhang = new PersonNode();
		xiaozhang.setId(3L);
		xiaozhang.setAge((short) 20);
		xiaozhang.setName("小张");
		final PersonNode xiaowang = new PersonNode();
		xiaowang.setId(4L);
		xiaowang.setAge((short) 20);
		xiaowang.setName("小王");
		final PersonNode laowang = new PersonNode();
		laowang.setId(5L);
		laowang.setAge((short) 40);
		laowang.setName("老王");
		// 小明
		xiaoming.setFriend(List.of(xiaozhang, xiaowang));
		final PersonRelationship qizi = new PersonRelationship();
		qizi.setId(1L);
		qizi.setName("妻子");
		qizi.setTarget(xiaohong);
		xiaoming.setRelative(List.of(qizi));
		// 小红
		final PersonRelationship zhangfu = new PersonRelationship();
		zhangfu.setId(2L);
		zhangfu.setName("丈夫");
		zhangfu.setTarget(xiaoming);
		xiaohong.setFriend(List.of(xiaowang));
		xiaohong.setRelative(List.of(zhangfu));
		// 小王
		final PersonRelationship parent = new PersonRelationship();
		parent.setId(3L);
		parent.setName("父亲");
		parent.setTarget(laowang);
		xiaowang.setRelative(List.of(parent));
		this.personRepository.saveAll(List.of(xiaoming, xiaohong, xiaozhang, xiaowang, laowang));
	}

	@Test
	public void testDelete() {
//		match (node) detach delete node
		this.personRepository.findAll().forEach(v -> this.personRepository.delete(v));
	}

	@Test
	public void testFind() {
		LOGGER.info("{}", this.personRepository.findByName("小王"));
		this.personRepository.findRelationship("小明", "老王").forEach(v -> LOGGER.info("{}", v));
	}

}
