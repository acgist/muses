package com.acgist.service.excel;

import com.acgist.service.BootExcelService.Formatter;

/**
 * 字符串格式化工具
 * 
 * @author acgist
 */
public class StringFormatter implements Formatter {

	@Override
	public Object parse(Object object) {
		if(object == null) {
			return Formatter.super.parse(object);
		}
		return object.toString();
	}
	
}