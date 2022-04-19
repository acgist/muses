package com.acgist.service.excel;

import com.acgist.service.BootExcelService.Formatter;

/**
 * Long格式化工具
 * 
 * @author acgist
 */
public class LongFormatter implements Formatter {

	@Override
	public Object parse(Object object) {
		if(object == null) {
			return Formatter.super.parse(object);
		}
		return Long.valueOf(object.toString());
	}

}
