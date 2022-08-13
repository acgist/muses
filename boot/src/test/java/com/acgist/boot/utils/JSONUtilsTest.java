package com.acgist.boot.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;

import com.acgist.boot.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
		dto.setDate(new Date());
		dto.setDateZoneId(dto.getDate());
		dto.setLocalDateTime(LocalDateTime.now());
		log.info("{}", JSONUtils.toJSON(dto));
		log.info("{}", JSONUtils.toJava(JSONUtils.toJSON(dto), DateDto.class));
		final DateDto format = JSONUtils.toJava(JSONUtils.toJSON(dto), DateDto.class);
		assertEquals(format.getDate(), format.getDateZoneId());
	}
	
	@Getter
	@Setter
	@ToString
	static class DateDto {
		
		@JsonFormat(pattern = "yyyyMMddHHmmss")
		@DateTimeFormat(pattern = "yyyyMMddHHmmss")
		private Date date;
		
		@JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
		@DateTimeFormat(pattern = "yyyyMMddHHmmss")
		private Date dateZoneId;
		
		@JsonFormat(pattern = "yyyyMMddHHmmss")
		@DateTimeFormat(pattern = "yyyyMMddHHmmss")
		private LocalDateTime localDateTime;

	}
	
}
