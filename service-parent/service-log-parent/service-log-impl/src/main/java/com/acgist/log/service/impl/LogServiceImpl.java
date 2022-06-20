package com.acgist.log.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.acgist.boot.service.FreemarkerService;
import com.acgist.boot.service.IdService;
import com.acgist.boot.utils.DateUtils;
import com.acgist.boot.utils.JSONUtils;
import com.acgist.log.api.ILogService;
import com.acgist.log.config.MappingConfig;
import com.acgist.log.config.TableMapping;
import com.acgist.log.dao.es.LogRepository;
import com.acgist.log.model.es.Log;
import com.acgist.log.model.mapstruct.LogMapstruct;
import com.acgist.log.model.message.LogMessage;
import com.acgist.log.model.query.Query;
import com.acgist.log.model.vo.LogVo;
import com.acgist.log.service.LogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogServiceImpl implements LogService, ILogService {

	@Autowired
	private IdService idService;
	@Autowired
	private LogMapstruct logMapstruct;
	@Autowired
	private MappingConfig mappingConfig;
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private FreemarkerService freemarkerService;
	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	
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
		final TableMapping tableMapping = this.mappingConfig.getMapping().get(table);
		final Iterator<Object> oldIterator = logMessage.getOld() == null ? null : logMessage.getOld().iterator();
		final Iterator<Object> dataIterator = logMessage.getData() == null ? null : logMessage.getData().iterator();
		while(
			(oldIterator != null && oldIterator.hasNext()) ||
			(dataIterator != null && dataIterator.hasNext())
		) {
			final Map<String, Object> old = oldIterator == null ? null : (Map<String, Object>) oldIterator.next();
			final Map<String, Object> data = dataIterator == null ? null : (Map<String, Object>) dataIterator.next();
			this.columnToField(tableMapping, old, data);
			// 映射数据
			final Log log = new Log();
			log.setId(this.idService.id());
			log.setType(type);
			log.setTable(table);
			final String sourceJson = JSONUtils.toJSON(data);
			final Object sourceObject = JSONUtils.toJava(sourceJson, tableMapping.getClazz());
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
	 * 数据映射替换
	 * 
	 * @param tableMapping 数据映射
	 * @param old 旧的数据
	 * @param data 新的数据
	 */
	private void columnToField(TableMapping tableMapping, Map<String, Object> old, Map<String, Object> data) {
		tableMapping.getColumnMap().forEach((key, mapping) -> {
			final Object oldValue = old == null ? null : old.remove(key);
			final Object dataValue = data == null ? null : data.remove(key);
			if(oldValue != null) {
				old.put(mapping.getField(), oldValue);
			}
			if(dataValue != null) {
				data.put(mapping.getField(), dataValue);
			}
		});
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
		// TODO：删除
	}
	
	@Override
	public List<LogVo> query(Query query) {
		final Class<Log> entityClass = Log.class;
		final QueryBuilder queryBuilder = this.builder(query);
		final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
		final IndexCoordinates indexCoordinates = this.elasticsearchOperations.getIndexCoordinatesFor(entityClass);
		// 分页信息
//		final long count = this.elasticsearchOperations.count(searchQuery, entityClass, indexCoordinates);
//		if (count == 0L) {
//			return List.of();
//		}
		// 注意分页：从零开始
		final PageRequest pageRequest = PageRequest.of(
			query.getPage(),
			query.getPageSize(),
			Sort.by(Order.desc("id"))
		);
		searchQuery.setPageable(pageRequest);
		final SearchHits<Log> searchHits = this.elasticsearchOperations.search(searchQuery, entityClass, indexCoordinates);
		final SearchPage<Log> searchPage = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
		return this.toPageVo(searchPage);
	}
	
	/**
	 * 构建查询语句
	 * 
	 * @param query 查询语句
	 * 
	 * @return ES查询语句
	 */
	private QueryBuilder builder(Query query) {
		final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if(ArrayUtils.isNotEmpty(query.getId())) {
			queryBuilder.must(QueryBuilders.termsQuery("id", ArrayUtils.toPrimitive(query.getId())));
		}
		if(ArrayUtils.isNotEmpty(query.getTable())) {
			queryBuilder.must(QueryBuilders.termsQuery("table", query.getTable()));
		}
		if(ArrayUtils.isNotEmpty(query.getSourceId())) {
			queryBuilder.must(QueryBuilders.termsQuery("sourceId", ArrayUtils.toPrimitive(query.getSourceId())));
		}
		if(StringUtils.isNotEmpty(query.getKeyword())) {
			queryBuilder.must(QueryBuilders.matchQuery("sourceName", QueryParser.escape(query.getKeyword())));
		}
		if(query.getCreateDate() != null) {
			queryBuilder.must(
				QueryBuilders.rangeQuery("createDate")
				.gte(DateUtils.toMilli(query.getCreateDate()[0]))
				.lte(DateUtils.toMilli(query.getCreateDate()[1]))
			);
		}
		return queryBuilder;
	}
	
	/**
	 * 类型转换
	 * 
	 * @param searchPage 搜索结果
	 * 
	 * @return DTO结果
	 */
	private List<LogVo> toPageVo(SearchPage<Log> searchPage) {
		final List<Log> logs = searchPage.getContent().stream().map(SearchHit::getContent).collect(Collectors.toList());
		final List<LogVo> list = this.logMapstruct.toVo(logs);
		list.forEach(this::complet);
		return list;
	}
	
	/**
	 * 补全信息
	 * 
	 * @param logVo 日志
	 */
	private void complet(LogVo logVo) {
		final String table = logVo.getTable();
		final TableMapping tableMapping = this.mappingConfig.getMapping().get(table);
		if(tableMapping == null) {
			return;
		}
		// 基本信息
		final Class<?> clazz = tableMapping.getClazz();
		final String diffValue = logVo.getDiffValue();
		final String sourceValue = logVo.getSourceValue();
		if(StringUtils.isNotEmpty(sourceValue)) {
			logVo.setSourceObject(JSONUtils.toJava(sourceValue, clazz));
		}
		// 差异信息
		if(StringUtils.isNotEmpty(diffValue)) {
			final Map<String, Object> diffMap = JSONUtils.toMap(diffValue);
			final Map<String, Object> sourceMap = StringUtils.isEmpty(sourceValue) ? new HashMap<>() : JSONUtils.toMap(sourceValue);
			sourceMap.putAll(diffMap);
			logVo.setDiffMap(diffMap);
			logVo.setDiffObject(JSONUtils.toJava(JSONUtils.toJSON(sourceMap), clazz));
		}
		// 日志信息
		final Map<String, Object> map = new HashMap<>();
		map.put("log", logVo);
		map.put("diff", logVo.getDiffObject());
		map.put("source", logVo.getSourceObject());
		map.put("diffMap", logVo.getDiffMap());
		map.put("mapping", tableMapping);
		map.put("fieldMap", tableMapping.getFieldMap());
		map.put("columnMap", tableMapping.getColumnMap());
		logVo.setLog(this.freemarkerService.buildTemplate(tableMapping.getTemplate(logVo), map));
	}

}
