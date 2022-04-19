package com.acgist.service.excel;

import java.util.Date;

import com.acgist.boot.DateUtils;
import com.acgist.boot.config.FormatStyle.DateStyle;
import com.acgist.service.BootExcelService.Formatter;

/**
 * 日期格式化工具
 * 
 * @author acgist
 */
public class DateFormatter implements Formatter {

	@Override
	public String format(Object object) {
		if(object instanceof Date) {
			// TODO：JDK17
			return DateUtils.format((Date) object, DateStyle.YYYY_MM_DD.getFormat());
		} else {
			return Formatter.super.format(object);
		}
	}
	
	@Override
	public Object parse(Object object) {
		if(object == null) {
			return Formatter.super.parse(object);
		}
		return DateUtils.parse(object.toString(), DateStyle.YYYY_MM_DD.getFormat());
	}
	
}
