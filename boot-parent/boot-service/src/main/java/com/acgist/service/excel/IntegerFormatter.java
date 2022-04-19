package com.acgist.service.excel;

import com.acgist.service.BootExcelService.Formatter;

/**
 * Integer格式化工具
 * 
 * @author acgist
 */
public class IntegerFormatter implements Formatter {

	@Override
	public Object parse(Object object) {
		if(object == null) {
			return Formatter.super.parse(object);
		}
		return Integer.valueOf(object.toString());
	}
	
}
