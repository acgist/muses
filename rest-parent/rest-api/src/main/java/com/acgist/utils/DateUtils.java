package com.acgist.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>工具 - 时间</p>
 * 
 * @author acgist
 */
public final class DateUtils {

	public static final String FORMAT_PATTERN = "yyyyMMddHHmmss";
	
	private DateUtils() {
	}
	
	/**
	 * <p>生成时间戳</p>
	 * 
	 * @return 时间戳
	 */
	public static final String buildTime() {
		return buildTime(new Date());
	}
	
	/**
	 * <p>生成时间戳</p>
	 * 
	 * @param date 时间
	 * 
	 * @return 时间戳
	 */
	public static final String buildTime(Date date) {
		if(date == null) {
			return null;
		}
		final SimpleDateFormat formater = new SimpleDateFormat(FORMAT_PATTERN);
		return formater.format(date);
	}
	
}
