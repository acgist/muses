package com.acgist.service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.acgist.boot.config.FormatStyle.DateStyle;
import com.acgist.model.entity.BootEntity;
import com.acgist.model.query.FilterQuery;

import lombok.Getter;

/**
 * Excel Service
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
public interface ExcelService<T extends BootEntity> extends BootService<T> {

	/**
	 * 格式化工具Map
	 */
	Map<Class<? extends Formatter>, Formatter> FORMATTER = new HashMap<>();

	/**
	 * 格式化工具
	 * 
	 * @author acgist
	 */
	public interface Formatter {
		
		/**
		 * 格式化数据
		 * 
		 * @param object 原始数据
		 * 
		 * @return 输出文本
		 */
		String format(Object object);
		
	}
	
	/**
	 * 字符串格式化工具
	 * 
	 * @author acgist
	 */
	public class StringFormatter implements Formatter {

		@Override
		public String format(Object object) {
			return Objects.toString(object, "");
		}
		
	}

	/**
	 * 日期格式化工具
	 * 
	 * @author acgist
	 */
	public class LocalDateTimeFormatter implements Formatter {

		@Override
		public String format(Object object) {
			if(object instanceof TemporalAccessor) {
				// TODO：JDK17
				return DateStyle.YYYY_MM_DD.getDateTimeFormatter().format((TemporalAccessor) object);
			} else {
				return Objects.toString(object, "");
			}
		}
		
	}
	
	/**
	 * Excel表头
	 * 
	 * @author acgist
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	@Documented
	public @interface ExcelHeader {

		/**
		 * @return 表头名称
		 */
		String name();
		
		/**
		 * @return 格式化工具类型
		 */
		Class<? extends Formatter> formatter() default StringFormatter.class;
		
	}
	
	/**
	 * 字段信息
	 * 
	 * @author acgist
	 */
	@Getter
	public class ExcelHeaderValue {
		
		/**
		 * 表头名称
		 */
		private final String name;
		/**
		 * 格式化工具
		 */
		private final Formatter formatter;
		
		public ExcelHeaderValue(String name, Formatter formatter) {
			this.name = name;
			this.formatter = formatter;
		}
		
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
	void download(FilterQuery query, Map<String, ExcelHeaderValue> header, OutputStream output);
	
	/**
	 * 下载Excel
	 * 
	 * @param list 列表
	 * @param header 表头
	 * @param output 输出
	 * 
	 * @throws IOException IO异常 
	 */
	void download(List<T> list, Map<String, ExcelHeaderValue> header, OutputStream output) throws IOException;
	
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
	 * @return 表头
	 */
	Map<String, ExcelHeaderValue> header();
	
	/**
	 * 获取格式化工具
	 * 
	 * @param formatter 工具类型
	 * 
	 * @return 格式化工具
	 */
	default Formatter formatter(Class<? extends Formatter> formatter) {
		return FORMATTER.computeIfAbsent(formatter, key -> {
			try {
				return formatter.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// 忽略
			}
			return null;
		});
	}
	
}
