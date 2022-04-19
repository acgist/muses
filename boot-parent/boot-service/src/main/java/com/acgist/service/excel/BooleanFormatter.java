package com.acgist.service.excel;

import com.acgist.service.BootExcelService.Formatter;

/**
 * Boolean格式化工具
 * 
 * @author acgist
 */
public class BooleanFormatter implements Formatter {

	@Override
	public Object parse(Object object) {
		if(object == null) {
			return Formatter.super.parse(object);
		}
		return Boolean.valueOf(object.toString());
	}
	
}
