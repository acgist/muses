package com.acgist.boot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具
 * 
 * @author acgist
 */
public final class DateUtils {

	private DateUtils() {
	}

	/**
	 * 时间戳格式：yyyyMMddHHmmss
	 */
	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	/**
	 * 生成时间戳
	 * 
	 * @return 时间戳
	 * 
	 * @see #buildTime(LocalDateTime)
	 */
	public static final String buildTime() {
		return buildTime(LocalDateTime.now());
	}

	/**
	 * 生成时间戳
	 * 
	 * @param time 时间
	 * 
	 * @return 时间戳
	 * 
	 * @see #FORMAT
	 */
	public static final String buildTime(LocalDateTime time) {
		if (time == null) {
			return buildTime();
		}
		return time.format(FORMAT);
	}

}
