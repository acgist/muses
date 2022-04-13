package com.acgist.service.excel;

import java.time.temporal.TemporalAccessor;
import java.util.Objects;

import com.acgist.boot.config.FormatStyle.DateStyle;
import com.acgist.service.BootExcelService.Formatter;

/**
 * 日期格式化工具
 * 
 * @author acgist
 */
public class LocalDateTimeFormatter implements Formatter {

	@Override
	public String format(Object object) {
		if(object instanceof TemporalAccessor) {
			// TODO：JDK17
			return DateStyle.YYYY_MM_DD.getDateTimeFormatter().format((TemporalAccessor) object);
		} else {
			return Objects.toString(object, "");
		}
	}
	
	@Override
	public Object parse(Object object) {
		if(object == null) {
			return null;
		}
		return DateStyle.YYYY_MM_DD.getDateTimeFormatter().parse(object.toString());
	}
	
}
