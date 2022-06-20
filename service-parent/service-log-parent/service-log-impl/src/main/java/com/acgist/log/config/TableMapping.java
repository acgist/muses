package com.acgist.log.config;

import java.util.Map;

import com.acgist.log.model.vo.LogVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库表映射
 * 
 * @author acgist
 */
@Getter
@Setter
public class TableMapping {

	/**
	 * 类型
	 */
	private Class<?> clazz;
	/**
	 * 数据库表名称
	 */
	private String table;
	/**
	 * 数据库表别名
	 */
	private String tableName;
	/**
	 * 主键字段（默认：TableId）
	 */
	private String idField;
	/**
	 * 主键列名（默认：TableId）
	 */
	private String idColumn;
	/**
	 * 名称字段
	 */
	private String nameField;
	/**
	 * 名称列名
	 */
	private String nameColumn;
	/**
	 * 字段映射
	 */
	private Map<String, FieldMapping> fieldMap;
	/**
	 * 数据库列字段映射
	 */
	private Map<String, FieldMapping> columnMap;
	/**
	 * 最大历史保留版本（默认：30）
	 */
	private Integer maxHistoryVersion;
	/**
	 * 新增模板（默认：表名-insert.ftl）
	 */
	private String insertTemplate;
	/**
	 * 修改模板（默认：表名-update.ftl）
	 */
	private String updateTemplate;
	/**
	 * 删除模板（默认：表名-delete.ftl）
	 */
	private String deleteTemplate;
	
	/**
	 * 获取模板
	 * 
	 * @param logVo 日志
	 * 
	 * @return 模板
	 */
	public String getTemplate(LogVo logVo) {
		switch (logVo.getType()) {
		case INSERT:
			return this.insertTemplate;
		case UPDATE:
			return this.updateTemplate;
		case DELETE:
			return this.deleteTemplate;
		default:
			return null;
		}
	}
	
}