package com.acgist.service.excel;

import com.acgist.service.BootExcelService.Formatter;

/**
 * Double格式化工具
 * 
 * @author acgist
 */
public class DoubleFormatter implements Formatter {

	@Override
	public Object parse(Object object) {
		if(object == null) {
			return null;
		}
		return Double.valueOf(object.toString());
	}
	
}
