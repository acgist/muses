package com.acgist.boot.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.acgist.boot.config.FormatStyle.DateTimeStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtilsTest {
	
	@Test
	public void testParse() {
		assertNotNull(DateUtils.parse("20211212 12:12"));
		assertNotNull(DateUtils.parse("2021-12-12 12:12"));
		assertNotNull(DateUtils.parse("20211212121212"));
		assertNotNull(DateUtils.parse("20211212121212111"));
		assertNotNull(DateUtils.parse("20211212 12:12:12"));
		assertNotNull(DateUtils.parse("20211212 12:12:12.111"));
		assertNotNull(DateUtils.parse("2021-12-12 12:12:12"));
		assertNotNull(DateUtils.parse("2021-12-12 12:12:12.111"));
		assertNotNull(DateUtils.parse("2022-03-30 10:03:41"));
		assertNotNull(DateUtils.parse("2022-03-30T10:03:41"));
		assertNotNull(DateUtils.parse("2022-03-30T10:03:41Z"));
		final LocalDateTime now = LocalDateTime.now();
		final DateTimeStyle[] values = DateTimeStyle.values();
		for (DateTimeStyle dateTimeStyle : values) {
			final String format = dateTimeStyle.getDateTimeFormatter().format(now);
			final String rollback = dateTimeStyle.getDateTimeFormatter().format(DateUtils.parse(format));
			log.info("{} - {} - {} - {}", format, rollback, now, dateTimeStyle.getFormat());
			// 忽略UTC
			if(dateTimeStyle.getFormat().indexOf('Z') < 0) {
				assertEquals(
					format,
					rollback
				);
			}
		}
	}
	
}
