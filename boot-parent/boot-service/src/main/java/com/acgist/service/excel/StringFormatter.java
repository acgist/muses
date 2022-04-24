package com.acgist.service.excel;

import java.text.DecimalFormat;

import com.acgist.service.BootExcelService.Formatter;

/**
 * 字符串格式化工具
 * 
 * @author acgist
 */
public class StringFormatter implements Formatter {

	@Override
	public Object parseProxy(Object object) {
		if(object instanceof Double) {
			final DecimalFormat format = new DecimalFormat("#.######");
			return format.format(object);
		}
		return object.toString();
	}
	
}