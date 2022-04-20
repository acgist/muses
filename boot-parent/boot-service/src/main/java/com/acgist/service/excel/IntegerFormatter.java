package com.acgist.service.excel;

import com.acgist.service.BootExcelService.Formatter;

/**
 * Integer格式化工具
 * 
 * @author acgist
 */
public class IntegerFormatter implements Formatter {

	@Override
	public Object parseProxy(Object object) {
//		Double.valueOf(object.toString()).intValue();
//		new BigDecimal(object.toString()).intValue();
		return Integer.valueOf(object.toString());
	}
	
}
