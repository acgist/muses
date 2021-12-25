package com.acgist.data;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.boot.pojo.bean.User;

public class TemplateQueryUtilsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateQueryUtils.class);
	
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
