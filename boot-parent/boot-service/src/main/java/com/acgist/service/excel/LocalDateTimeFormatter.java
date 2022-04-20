package com.acgist.service.excel;

import java.time.temporal.TemporalAccessor;

import com.acgist.boot.config.FormatStyle.DateStyle;
import com.acgist.service.BootExcelService.Formatter;

/**
 * 日期格式化工具
 * 
 * @author acgist
 */
public class LocalDateTimeFormatter implements Formatter {

	@Override
	public String formatProxy(Object object) {
		// TODO：JDK17
		return DateStyle.YYYY_MM_DD.getDateTimeFormatter().format((TemporalAccessor) object);
	}
	
	@Override
	public Object parseProxy(Object object) {
		return DateStyle.YYYY_MM_DD.getDateTimeFormatter().parse(object.toString());
	}
	
}
