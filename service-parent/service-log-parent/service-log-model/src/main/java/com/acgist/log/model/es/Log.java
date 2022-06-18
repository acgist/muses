package com.acgist.log.model.es;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.acgist.model.es.BootDocument;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 日志索引
 * 
 * @author acgist
 */
@Getter
@Setter
@Document(indexName = "index_log")
@EqualsAndHashCode(callSuper = true)
public class Log extends BootDocument {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 操作类型
	 * 
	 * @author acgist
	 */
	public enum Type {
		
		// 新增
		INSERT,
		// 更新
		UPDATE,
		// 删除
		DELETE;
		
		/**
		 * 获取操作类型
		 * 
		 * @param type 类型
		 * 
		 * @return 操作类型
		 */
		public static final Type of(String type) {
			final Type[] values = Type.values();
			for (Type value : values) {
				if(value.name().equalsIgnoreCase(type)) {
					return value;
				}
			}
			return null;
		}
		
	}

	/**
	 * 类型
	 */
	@Field(type = FieldType.Keyword)
	private Type type;
	/**
	 * 原始表名
	 */
	@Field(type = FieldType.Keyword)
	private String table;
	/**
	 * 原始数据ID
	 */
	@Field(type = FieldType.Long)
	private Long sourceId;
	/**
	 * 原始数据名称
	 * 
	 * TODO：中文分词
	 */
	@Field(type = FieldType.Text)
	private String sourceName;
	/**
	 * 原始数据
	 */
	@Field(type = FieldType.Text, index = false)
	private String sourceValue;
	/**
	 * 差异数据
	 */
	@Field(type = FieldType.Text, index = false)
	private String diffValue;
	
}
