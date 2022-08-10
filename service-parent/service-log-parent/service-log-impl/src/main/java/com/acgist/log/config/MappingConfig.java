package com.acgist.log.config;

import java.lang.reflect.Field;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.acgist.model.dto.TableColumnDto;
import com.acgist.model.dto.TableDto;
import com.acgist.service.DatabaseService;
import com.acgist.transfer.Transfer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 映射配置
 * 
 * 优先使用配置文件，然后读取注解，最后读取数据库的注释。
 * 
 * @author acgist
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties("system.log")
public class MappingConfig {
	
	@Autowired
	private DatabaseService databaseService;
	
	/**
	 * 最大历史保留版本（默认：30）
	 */
	private Integer maxHistoryVersion;
	/**
	 * 新增模板（默认：表名/insert.ftl）
	 */
	private String insertTemplate;
	/**
	 * 修改模板（默认：表名/update.ftl）
	 */
	private String updateTemplate;
	/**
	 * 删除模板（默认：表名/delete.ftl）
	 */
	private String deleteTemplate;
	/**
	 * 需要记录的表
	 */
	private List<String> tables;
	/**
	 * 数据映射
	 */
	private Map<String, TableMapping> mapping;
	
	/**
	 * 是否记录数据库表
	 * 
	 * @param table 表名
	 * 
	 * @return 是否记录
	 */
	public boolean logTable(String table) {
		return this.tables.contains(table);
	}

	/**
	 * 加载实体
	 * 
	 * @param tableName 表名
	 * @param clazz 类型
	 */
	public void loadEntity(TableName tableName, Class<?> clazz) {
		final String name = tableName.value();
		final TableMapping tableMapping = this.mapping.computeIfAbsent(name, key -> new TableMapping());
		this.loadTableConfig(name, clazz, tableMapping);
	}

	/**
	 * 加载表的基本数据
	 * 
	 * @param tableName 表名
	 * @param clazz 类型
	 * @param tableMapping 数据映射
	 */
	private void loadTableConfig(String tableName, Class<?> clazz, TableMapping tableMapping) {
		// 设置表名
		if(StringUtils.isEmpty(tableMapping.getTable())) {
			tableMapping.setTable(tableName);
		}
		// 数据库表
		final TableDto tableDto;
		try {
			tableDto = this.databaseService.table(tableMapping.getTable());
		} catch (SQLException e) {
			log.warn("数据库表加载异常：{}", tableMapping.getTable(), e);
			this.tables.remove(tableName);
			this.mapping.remove(tableName);
			return;
		}
		// 设置名称
		if(StringUtils.isEmpty(tableMapping.getTableName())) {
			tableMapping.setTableName(tableDto.getComment());
		}
		// 设置类型
		if(tableMapping.getClazz() == null) {
			tableMapping.setClazz(clazz);
		}
		// 设置最大历史保留版本
		if(tableMapping.getMaxHistoryVersion() == null) {
			tableMapping.setMaxHistoryVersion(Objects.requireNonNullElse(this.maxHistoryVersion, 30));
		}
		// 设置模板
		if(StringUtils.isEmpty(tableMapping.getInsertTemplate())) {
			tableMapping.setInsertTemplate(this.getTemplate(tableName, "insert.ftl"));
		}
		if(StringUtils.isEmpty(tableMapping.getUpdateTemplate())) {
			tableMapping.setUpdateTemplate(this.getTemplate(tableName, "update.ftl"));
		}
		if(StringUtils.isEmpty(tableMapping.getDeleteTemplate())) {
			tableMapping.setDeleteTemplate(this.getTemplate(tableName, "delete.ftl"));
		}
		// 设置字段
		FieldUtils.getAllFieldsList(clazz).stream()
		.forEach(value -> this.loadTableFieldAndColumn(clazz, value, tableDto, tableMapping));
		// 名称映射
		final Map<String, FieldMapping> fieldMappingMap = tableMapping.getFieldMap();
		final Map<String, FieldMapping> columnMappingMap = tableMapping.getColumnMap();
		if(
			StringUtils.isEmpty(tableMapping.getNameField()) &&
			StringUtils.isEmpty(tableMapping.getNameColumn())
		) {
			// 不做任何操作
			log.info("数据库表没有指定数据名称字段：{}", tableName);
		} else if(
			StringUtils.isEmpty(tableMapping.getNameField()) &&
			columnMappingMap.get(tableMapping.getNameColumn()) != null
		) {
			tableMapping.setNameField(columnMappingMap.get(tableMapping.getNameColumn()).getField());
		} else if(
			StringUtils.isEmpty(tableMapping.getNameColumn()) &&
			fieldMappingMap.get(tableMapping.getNameField()) != null
		) {
			tableMapping.setNameColumn(fieldMappingMap.get(tableMapping.getNameField()).getColumn());
		}
	}
	
	/**
	 * 加载表的字段
	 * 
	 * @param clazz 类型
	 * @param field 字段
	 * @param tableDto 数据库表
	 * @param tableMapping 数据映射
	 */
	private void loadTableFieldAndColumn(Class<?> clazz, Field field, TableDto tableDto, TableMapping tableMapping) {
		final TableId tableId = field.getAnnotation(TableId.class);
		final TableField tableField = field.getAnnotation(TableField.class);
		// 属性
		final String fieldName = field.getName();
		// 列名
		String columnName = fieldName;
		if(tableId != null) {
			columnName = Objects.requireNonNullElse(tableId.value(), fieldName);
			if(
				StringUtils.isEmpty(tableMapping.getIdField()) &&
				StringUtils.isEmpty(tableMapping.getIdColumn())
			) {
				tableMapping.setIdField(fieldName);
				tableMapping.setIdColumn(columnName);
			} else if(StringUtils.isEmpty(tableMapping.getIdField())) {
				tableMapping.setIdField(fieldName);
			} else if(StringUtils.isEmpty(tableMapping.getIdColumn())) {
				tableMapping.setIdColumn(columnName);
			}
		} else if(tableField != null) {
			columnName = Objects.requireNonNullElse(tableField.value(), fieldName);
		}
		if(StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(columnName)) {
			log.info("忽略字段：{}-{}-{}", clazz, fieldName, columnName);
			return;
		}
		// 字段映射
		if(tableMapping.getFieldMap() == null) {
			tableMapping.setFieldMap(new HashMap<>());
		}
		if(tableMapping.getColumnMap() == null) {
			tableMapping.setColumnMap(new HashMap<>());
		}
		final Map<String, FieldMapping> fieldMappingMap = tableMapping.getFieldMap();
		final Map<String, FieldMapping> columnMappingMap = tableMapping.getColumnMap();
		final FieldMapping fieldFieldMapping = fieldMappingMap.get(fieldName);
		final FieldMapping columnFieldMapping = columnMappingMap.get(columnName);
		final FieldMapping fieldMapping = fieldFieldMapping != null ? fieldFieldMapping : columnFieldMapping != null ? columnFieldMapping : new FieldMapping();
		if(StringUtils.isEmpty(fieldMapping.getName())) {
			final TableColumnDto tableColumnDto = tableDto.getColumn(columnName);
			fieldMapping.setName(tableColumnDto == null ? null : tableColumnDto.getComment());
		}
		if(StringUtils.isEmpty(fieldMapping.getField())) {
			fieldMapping.setField(fieldName);
		}
		if(StringUtils.isEmpty(fieldMapping.getColumn())) {
			fieldMapping.setColumn(columnName);
		}
		if(fieldMapping.getClazz() == null) {
			fieldMapping.setClazz(field.getType());
		}
		if(fieldMapping.getTransfer() == null) {
			final Transfer transfer = field.getAnnotation(Transfer.class);
			if(transfer != null) {
				fieldMapping.setTransfer(transfer.group());
			}
		}
		fieldMappingMap.put(fieldName, fieldMapping);
		columnMappingMap.put(columnName, fieldMapping);
	}
	
	/**
	 * 获取模板文件
	 * 
	 * @param table 数据库表
	 * @param name 模板文件名称
	 * 
	 * @return 完整文件名称
	 */
	private String getTemplate(String table, String name) {
		final String template = "/" + table + "/" + name;
		final URL resource = this.getClass().getResource("/templates" + template);
		if(resource != null) {
			return template;
		}
		return name;
	}
	
}
