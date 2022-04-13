package com.acgist.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.acgist.boot.BeanUtils;
import com.acgist.boot.StringUtils;
import com.acgist.boot.model.MessageCodeException;
import com.acgist.model.entity.BootEntity;
import com.acgist.model.query.FilterQuery;
import com.acgist.service.excel.StringFormatter;

import lombok.Getter;

/**
 * Excel Service
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
public interface BootExcelService<T extends BootEntity> extends BootService<T> {

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
		default String format(Object object) {
			return Objects.toString(object, "");
		}
		
		/**
		 * 加载数据
		 * 
		 * @param object 原始数据
		 * 
		 * @return 输入数据
		 */
		default Object parse(Object object) {
			return Objects.toString(object, null);
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
		 * @return 导入表头名称
		 */
		String loadName() default "";
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
		 * 字段名称
		 */
		private final String field;
		/**
		 * 表头名称
		 */
		private final String name;
		/**
		 * 导入表头名称
		 */
		private final String loadName;
		/**
		 * 格式化工具
		 */
		private final Formatter formatter;
		
		/**
		 * @param field 字段名称
		 * @param name 表头名称：name > loadName > field
		 * @param loadName 导入表头名称：loadName > name > field
		 * @param formatter
		 */
		public ExcelHeaderValue(String field, String name, String loadName, Formatter formatter) {
			this.field = field;
			this.name = StringUtils.isNotEmpty(name) ? name : StringUtils.isNotEmpty(loadName) ? loadName : field;
			this.loadName = StringUtils.isNotEmpty(loadName) ? loadName : StringUtils.isNotEmpty(name) ? name : field;
			this.formatter = formatter;
		}
		
	}
	
	/**
	 * 下载Excel模板
	 * 
	 * @param output 输出
	 */
	default void downloadTemplate(OutputStream output) {
		try {
			this.download(List.of(), this.header(), output);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "下载Excel失败");
		}
	}
	
	/**
	 * 下载Excel
	 * 
	 * @param query 条件
	 * @param output 输出
	 */
	default void download(FilterQuery query, OutputStream output) {
		try {
			this.download(this.list(query), this.header(), output);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "下载Excel失败");
		}
	}
	
	/**
	 * 下载Excel
	 * 
	 * @param query 条件
	 * @param header 表头
	 * @param output 输出
	 */
	default void download(FilterQuery query, Map<String, ExcelHeaderValue> header, OutputStream output) {
		try {
			this.download(this.list(query), header, output);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "下载Excel失败");
		}
	}
	
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
		return FORMATTER.computeIfAbsent(formatter, key -> BeanUtils.newInstance(formatter));
	}
	
	/**
	 * 加载Excel
	 * 
	 * @param path 路径
	 * 
	 * @return 数据
	 */
	default List<List<Object>> load(String path) {
		try {
			return this.load(new FileInputStream(path), 0);
		} catch (FileNotFoundException e) {
			throw MessageCodeException.of("读取Excel文件异常", e);
		}
	}
	
	/**
	 * 加载Excel
	 * 
	 * @param input 输入流
	 * @param sheet sheet
	 * 
	 * @return 数据
	 */
	List<List<Object>> load(InputStream input, int sheet);

	/**
	 * 加载Excel
	 * 
	 * @param path 路径
	 * 
	 * @return 数据
	 */
	default List<T> loadEntity(String path) {
		return this.load(this.load(path), this.getEntityClass(), this.header());
	}
	
	/**
	 * 加载Excel
	 * 
	 * @param <K> 类型
	 * 
	 * @param path 路径
	 * @param clazz 返回类型
	 * 
	 * @return 数据
	 */
	default <K> List<K> load(String path, Class<K> clazz) {
		return this.load(this.load(path), clazz, this.header());
	}
	
	/**
	 * 加载Excel
	 * 
	 * @param <K> 类型
	 * 
	 * @param list 数据：必须包含头部
	 * @param clazz 返回类型
	 * @param header 表头
	 * 
	 * @return 数据
	 */
	<K> List<K> load(List<List<Object>> list, Class<K> clazz, Map<String, ExcelHeaderValue> header);
	
}
