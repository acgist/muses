package com.acgist.service.excel;

import java.util.Date;

import com.acgist.boot.config.FormatStyle.DateStyle;
import com.acgist.boot.utils.DateUtils;
import com.acgist.service.BootExcelService.Formatter;

/**
 * 日期格式化工具
 * 
 * @author acgist
 */
public class DateFormatter implements Formatter {

	@Override
	public String formatProxy(Object object) {
		return DateUtils.format((Date) object, DateStyle.YYYY_MM_DD.getFormat());
	}
	
	@Override
	public Object parseProxy(Object object) {
		return DateUtils.parse(object.toString(), DateStyle.YYYY_MM_DD.getFormat());
	}
	
}
