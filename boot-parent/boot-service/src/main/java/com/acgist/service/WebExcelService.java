package com.acgist.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.model.entity.BootEntity;
import com.acgist.model.query.FilterQuery;
import com.acgist.service.impl.ExcelServiceImpl;

/**
 * Web Excel Service
 * 
 * @author acgist
 *
 * @param <T> 类型
 * 
 * @see ExcelServiceImpl
 */
public interface WebExcelService<T extends BootEntity> extends ExcelService<T> {

	/**
	 * 下载Excel
	 * 
	 * @param query 条件
	 * @param response 响应
	 */
	default void download(FilterQuery query, HttpServletResponse response) {
		final Map<String, String> header = this.header();
		this.download(query, header, response);
	}
	
	/**
	 * 下载Excel
	 * 
	 * @param query 条件
	 * @param header 表头
	 * @param response 响应
	 */
	default void download(FilterQuery query, Map<String, String> header, HttpServletResponse response) {
		final List<T> list = this.list(query);
		try {
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=" + this.getEntityClass().getSimpleName() + ".xlsx");
			this.download(list, header, response.getOutputStream());
		} catch (IOException e) {
			throw MessageCodeException.of(e, "下载Excel失败");
		}
	}
	
}
