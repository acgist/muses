package com.acgist.service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.acgist.model.entity.BootEntity;
import com.acgist.model.query.FilterQuery;

/**
 * Excel Service
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
public interface ExcelService<T extends BootEntity> extends BootService<T> {

	/**
	 * Excel表头
	 * 
	 * @author acgist
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface ExcelHeader {

		/**
		 * @return 表头名称
		 */
		String name();
		
	}
	
	/**
	 * 下载Excel
	 * 
	 * @param query 条件
	 * @param output 输出
	 */
	void download(FilterQuery query, OutputStream output);
	
	/**
	 * 下载Excel
	 * 
	 * @param query 条件
	 * @param header 表头
	 * @param output 输出
	 */
	void download(FilterQuery query, Map<String, String> header, OutputStream output);
	
	/**
	 * 下载Excel
	 * 
	 * @param list 列表
	 * @param header 表头
	 * @param output 输出
	 * 
	 * @throws IOException IO异常 
	 */
	void download(List<T> list, Map<String, String> header, OutputStream output) throws IOException;
	
	/**
	 * 表头表格样式
	 * 
	 * @param workbook 表格
	 * 
	 * @return 表头表格样式
	 */
	CellStyle headerCellStyle(XSSFWorkbook workbook);
	
	/**
	 * 数据表格样式
	 * 
	 * @param workbook 表格
	 * 
	 * @return 数据表格样式
	 */
	CellStyle dataCellStyle(XSSFWorkbook workbook);
	
	/**
	 * 获取表头信息，默认使用注解获取，如果注解为空获取全部属性，可以重写方法自定义表头。
	 * 
	 * 字段名称 = 表格列名
	 * 
	 * @return 表头
	 */
	Map<String, String> header();
	
}
