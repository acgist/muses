package com.acgist.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.model.entity.BootEntity;
import com.acgist.model.query.FilterQuery;
import com.acgist.service.impl.BootExcelServiceImpl;

/**
 * Web Excel Service
 * 
 * @author acgist
 *
 * @param <T> 类型
 * 
 * @see BootExcelServiceImpl
 */
public interface BootWebExcelService<T extends BootEntity> extends BootExcelService<T> {

	/**
	 * 下载Excel
	 * 
	 * @param name 文件名称
	 * @param query 条件
	 * @param response 响应
	 */
	default void download(String name, FilterQuery query, HttpServletResponse response) {
		final Map<String, ExcelHeaderValue> header = this.header();
		this.download(name, query, header, response);
	}
	
	/**
	 * 下载Excel
	 * 
	 * @param name 文件名称
	 * @param query 条件
	 * @param header 表头
	 * @param response 响应
	 */
	default void download(String name, FilterQuery query, Map<String, ExcelHeaderValue> header, HttpServletResponse response) {
		final List<T> list = this.list(query);
		try {
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=" + Objects.toString(name, this.getEntityClass().getSimpleName()) + ".xlsx");
			this.download(list, header, response.getOutputStream());
		} catch (IOException e) {
			throw MessageCodeException.of(e, "下载Excel失败");
		}
	}
	
}
