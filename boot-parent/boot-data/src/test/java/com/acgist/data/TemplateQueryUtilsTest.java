package com.acgist.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.pojo.bean.User;

public class TemplateQueryUtilsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateQueryUtils.class);
	
	@Test
	public void testSelectQuery() {
		assertTrue(TemplateQueryUtils.selectQuery("select * from t_user"));
		assertTrue(TemplateQueryUtils.selectQuery("SELECT * from t_user"));
		assertFalse(TemplateQueryUtils.selectQuery("insert into t_user values (...)"));
		assertFalse(TemplateQueryUtils.selectQuery("delete from t_user where ..."));
	}

	@Test
	public void testFilterParamterMap() {
		final Map<String, Object> result = TemplateQueryUtils.filterParamterMap(
			"select * from t_user where name = :name "
			+ "and age = :age \n"
			+ "and :root :acgist, :id) :end",
			Map.of(
				"name", "name",
				"end", "end",
				"age", "age",
				"root", "root",
				"acgist", "acgist",
				"id", "id",
				"filter", "filter"
			)
		);
		assertEquals(6, result.size());
		LOGGER.info("{}", result);
	}
	
	@Test
	public void testCompare() {
		assertEquals(0, TemplateQueryUtils.compare(null, null));
		assertEquals(0, TemplateQueryUtils.compare("null", null));
		assertEquals(0, TemplateQueryUtils.compare("null", "null"));
		assertEquals(0, TemplateQueryUtils.compare("acgist", "acgist"));
		assertEquals(-1, TemplateQueryUtils.compare("a", "b"));
		assertEquals(1, TemplateQueryUtils.compare("c", "b"));
		assertEquals(-1, TemplateQueryUtils.compare(0, "1"));
		assertEquals(0, TemplateQueryUtils.compare(1, "1"));
		assertEquals(1, TemplateQueryUtils.compare(2, "1"));
	}
	
	@Test
	public void testCondition() {
		final Map<String, Object> map = new HashMap<>();
		map.put("date", new Date());
		map.put("bool", Boolean.TRUE);
		map.put("number", 100);
		map.put("string", "acgist");
		assertFalse(TemplateQueryUtils.condition(map, "date == null"));
		assertFalse(TemplateQueryUtils.condition(map, "date==null"));
		assertFalse(TemplateQueryUtils.condition(map, "date== null"));
		assertFalse(TemplateQueryUtils.condition(map, "date ==null"));
		assertTrue(TemplateQueryUtils.condition(map, "date != null"));
		assertTrue(TemplateQueryUtils.condition(map, " bool "));
		assertTrue(TemplateQueryUtils.condition(map, "bool"));
		assertTrue(TemplateQueryUtils.condition(map, "number > 1"));
		assertTrue(TemplateQueryUtils.condition(map, "number >= 1"));
		assertTrue(TemplateQueryUtils.condition(map, "number >= 100"));
		assertTrue(TemplateQueryUtils.condition(map, "number == 100"));
		assertTrue(TemplateQueryUtils.condition(map, "number <= 100"));
		assertTrue(TemplateQueryUtils.condition(map, "number <= 111"));
		assertTrue(TemplateQueryUtils.condition(map, "number < 111"));
		assertFalse(TemplateQueryUtils.condition(map, "number < 1"));
		assertFalse(TemplateQueryUtils.condition(map, "number > 111"));
		assertTrue(TemplateQueryUtils.condition(map, "string == acgist"));
		assertTrue(TemplateQueryUtils.condition(map, "number != null"));
		assertTrue(TemplateQueryUtils.condition(map, "number != root"));
	}
	
	@Test
	public void testBuildWhereLine() {
		final Map<String, Object> map = new HashMap<>();
		map.put("date", new Date());
		map.put("bool", Boolean.TRUE);
		map.put("number", 100);
		map.put("string", "acgist");
		assertNull(TemplateQueryUtils.buildWhereLine(map, "$(date == null) test"));
		assertNull(TemplateQueryUtils.buildWhereLine(map, "$(date==null) test"));
		assertNull(TemplateQueryUtils.buildWhereLine(map, "$(date== null) test"));
		assertNull(TemplateQueryUtils.buildWhereLine(map, "$(date ==null) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(date != null) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$( bool ) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(bool) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number > 1) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number >= 1) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number >= 100) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number == 100) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number <= 100) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number <= 111) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number < 111) test"));
		assertNull(TemplateQueryUtils.buildWhereLine(map, "$(number < 1) test"));
		assertNull(TemplateQueryUtils.buildWhereLine(map, "$(number > 111) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(string == acgist) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number != null) test"));
		assertNotNull(TemplateQueryUtils.buildWhereLine(map, "$(number != root) test"));
	}

	@Test
	public void testBuildWhere() {
		final Map<String, Object> map = new HashMap<>();
		map.put("date", new Date());
		map.put("bool", Boolean.TRUE);
		map.put("number", 100);
		map.put("string", "acgist");
		final String query = TemplateQueryUtils.buildWhere(
			"$(date == null) and date = :date \n"
			+ "$(bool) and bool = :bool \n"
			+ "$(number > 111) and age = :number \n"
			+ "$(string == acgist) and name = :name",
			map
		);
		LOGGER.info("{}", query);
	}
	
	@Test
	public void testBuildParamterMap() {
		final User user = new User();
		user.setId(100L);
		user.setName("acgist");
		final Map<String, Object> map = TemplateQueryUtils.buildParamterMap(
			new Object[] {
				1L,
				"1",
				"2",
				false,
				new Date(),
				user
			},
			new String[] {
				"age", "root", "acgist", "bool", "date", "user"
			}, 6);
		LOGGER.info("{}", map);
	}
	
}
