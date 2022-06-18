package com.acgist.log.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.boot.utils.JSONUtils;
import com.acgist.log.api.ILogService;
import com.acgist.log.config.MappingConfig;
import com.acgist.log.config.TableMapping;
import com.acgist.log.dao.es.LogRepository;
import com.acgist.log.model.es.Log;
import com.acgist.log.model.message.LogMessage;
import com.acgist.log.service.LogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogServiceImpl implements LogService, ILogService {

	@Autowired
	private MappingConfig mappingConfig;
	@Autowired
	private LogRepository logRepository;
	
	@Override
	@SuppressWarnings("unchecked")
	public void log(LogMessage logMessage) {
		final String table = logMessage.getTable();
		if(!this.mappingConfig.logTable(table)) {
			// 忽略
			return;
		}
		final Log.Type type = Log.Type.of(logMessage.getType());
		if(type == null) {
			log.warn("不支持的类型：{}", logMessage.getType());
			return;
		}
		final List<Log> logs = new ArrayList<>();
		final Iterator<Object> oldIterator = logMessage.getOld().iterator();
		final Iterator<Object> dataIterator = logMessage.getData().iterator();
		final TableMapping tableMapping = this.mappingConfig.getMapping().get(table);
		while(dataIterator.hasNext()) {
			final Map<String, Object> old = (Map<String, Object>) oldIterator.next();
			final Map<String, Object> data = (Map<String, Object>) dataIterator.next();
			tableMapping.getColumnMap().forEach((key, mapping) -> {
				final Object oldValue = old.remove(key);
				final Object dataValue = data.remove(key);
				if(oldValue != null) {
					old.put(mapping.getField(), oldValue);
				}
				if(dataValue != null) {
					data.put(mapping.getField(), dataValue);
				}
			});
			// 映射数据
			final Log log = new Log();
			final String sourceJson = JSONUtils.toJSON(data);
			final Object sourceObject = JSONUtils.toJava(sourceJson, tableMapping.getClazz());
			log.setType(type);
			log.setTable(table);
			final Object sourceId = this.getFieldValue(sourceObject, tableMapping.getIdField());
			log.setSourceId(sourceId == null ? null : Long.valueOf(sourceId.toString()));
			final Object sourceName = this.getFieldValue(sourceObject, tableMapping.getNameField());
			log.setSourceName(sourceName == null ? null : sourceName.toString());
			log.setSourceValue(sourceJson);
			log.setDiffValue(JSONUtils.toJSON(old));
			logs.add(log);
		}
		this.logRepository.saveAll(logs);
	}

	/**
	 * 读取字段属性
	 * 
	 * @param object 对象
	 * @param fieldName 字段
	 * 
	 * @return 字段属性
	 */
	private Object getFieldValue(Object object, String fieldName) {
		if(object == null || StringUtils.isEmpty(fieldName)) {
			return null;
		}
		try {
			final Field field = FieldUtils.getField(object.getClass(), fieldName, true);
			if(field == null) {
				return null;
			}
			return field.get(object);
		} catch (IllegalAccessException e) {
			log.error("字段读取异常：{}-{}", object, fieldName, e);
		}
		return null;
	}

	@Override
	public void deleteHistory(String table, Long sourceId) {
	}

}
