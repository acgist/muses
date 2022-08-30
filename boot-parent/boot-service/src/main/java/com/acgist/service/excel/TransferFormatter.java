package com.acgist.service.excel;

import java.util.Map.Entry;
import java.util.Objects;

import com.acgist.boot.utils.SpringUtils;
import com.acgist.service.BootExcelService.Formatter;
import com.acgist.service.TransferService;

/**
 * 枚举格式化工具
 * 
 * @author acgist
 */
public class TransferFormatter implements Formatter {

	/**
	 * 枚举分组
	 */
	private final ThreadLocal<String> group = new ThreadLocal<>();
	/**
	 * 枚举翻译服务
	 */
	private final TransferService transferService;
	
	public TransferFormatter() {
		this.transferService = SpringUtils.getBeanNullable(TransferService.class);
	}
	
	/**
	 * @param group 枚举分组
	 */
	public void group(String group) {
		this.group.set(group);
	}

	@Override
	public String formatProxy(Object object) {
		final String key = object.toString();
		return this.transferService.transfer(this.group.get(), key);
	}
	
	@Override
	public Object parseProxy(Object object) {
		return this.transferService.transfer(this.group.get()).entrySet().stream()
			.filter(entry -> Objects.equals(entry.getValue(), Objects.toString(object, null)))
			.map(Entry::getKey);
	}
	
}
