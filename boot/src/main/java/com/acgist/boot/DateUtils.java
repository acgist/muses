package com.acgist.boot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	
	private DateUtils() {
	}
	
	public static final String buildTime() {
		return buildTime(LocalDateTime.now());
	}
	
	public static final String buildTime(LocalDateTime time) {
		if(time == null) {
			return null;
		}
		return time.format(FORMAT);
	}
	
}
