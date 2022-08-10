package com.acgist.boot.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;

import com.acgist.boot.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONUtilsTest {

	@Test
	public void testJSON() {
		final User user = new User();
		user.setId(1L);
		user.setName("acgist");
		log.info("{}", user);
		final String json = JSONUtils.toJSON(user);
		log.info("{}", json);
//		log.info("{}", JSONUtils.toJava(json));
		log.info("{}", JSONUtils.toJava(json, User.class));
//		final User jsonUser = JSONUtils.toJava(json);
//		log.info("{}", jsonUser);
	}
	
	@Test
	public void testMap() throws JsonProcessingException {
		final Map<String, String> map = new HashMap<>();
		map.put("id", null);
		map.put("name", "acgist");
		log.info("{}", JSONUtils.toJSON(map));
		log.info("{}", JSONUtils.toJSONNullable(map));
		log.info("{}", JSONUtils.toMap(JSONUtils.toJSON(map)));
		log.info("{}", JSONUtils.toMap(JSONUtils.toJSONNullable(map)));
	}
	
	@Test
	public void testDate() {
		final DateDto dto = new DateDto();
		log.info("{}", JSONUtils.toJSON(dto));
	}
	
	static class DateDto {
		
		@JsonFormat(pattern = "yyyyMMddHHmmss")
		@DateTimeFormat(pattern = "yyyyMMddHHmmss")
		private LocalDateTime time = LocalDateTime.now();

		public LocalDateTime getTime() {
			return time;
		}

		public void setTime(LocalDateTime time) {
			this.time = time;
		}
		
	}
	
}
