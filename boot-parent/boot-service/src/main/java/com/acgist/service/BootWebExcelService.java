package com.acgist.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.acgist.boot.UrlUtils;
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
	 * 下载Excel模板
	 * 
	 * @param name 文件名称
	 * @param response 响应
	 */
	default void downloadTemplate(String name, HttpServletResponse response) {
		this.download(name, List.of(), this.header(), response);
	}
	
	/**
	 * 下载Excel
	 * 
	 * @param name 文件名称
	 * @param query 条件
	 * @param response 响应
	 */
	default void download(String name, FilterQuery query, HttpServletResponse response) {
		this.download(name, query, this.header(), response);
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
		this.download(name, this.list(query), header, response);
	}
	
	/**
	 * 下载Excel
	 * 
	 * @param name 文件名称
	 * @param list 数据
	 * @param header 表头
	 * @param response 响应
	 */
	default void download(String name, List<T> list, Map<String, ExcelHeaderValue> header, HttpServletResponse response) {
		try {
			final String downloadName = UrlUtils.encode(Objects.toString(name, this.getEntityClass().getSimpleName()));
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=" + downloadName + ".xlsx");
			this.download(list, header, response.getOutputStream());
		} catch (IOException e) {
			throw MessageCodeException.of(e, "下载Excel失败");
		}
	}
	
	/**
	 * 加载Excel
	 * 
	 * @param file 文件
	 * 
	 * @return 数据
	 */
	default List<List<Object>> load(MultipartFile file) {
		try {
			return this.load(file.getInputStream(), 0);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "读取Excel文件异常");
		}
	}
	
	/**
	 * 加载Excel
	 * 
	 * @param file 文件
	 * 
	 * @return 数据
	 */
	default List<T> loadEntity(MultipartFile file) {
		return this.load(this.load(file), this.getEntityClass(), this.header());
	}
	
	/**
	 * 加载Excel
	 * 
	 * @param <K> 类型
	 * 
	 * @param file 文件
	 * @param clazz 返回类型
	 * 
	 * @return 数据
	 */
	default <K> List<K> load(MultipartFile file, Class<K> clazz) {
		return this.load(this.load(file), clazz, this.header());
	}
	
}
