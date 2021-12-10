package com.acgist.boot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

/**
 * JSON工具
 * 
 * @author acgist
 */
public final class JSONUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

	private JSONUtils() {
	}

	/**
	 * Mapper（线程安全）
	 */
	private static final ObjectMapper MAPPER = buildMapper();

	/**
	 * Java转JSON
	 * 
	 * @param object Java
	 * 
	 * @return JSON
	 */
	public static final String toJSON(Object object) {
		if (object == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOGGER.error("toJSON异常：{}", object, e);
		}
		return null;
	}

	/**
	 * JSON转Java
	 * 
	 * @param <T>  Java类型
	 * @param json JSON
	 * 
	 * @return Java
	 */
	public static final <T> T toJava(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, new TypeReference<T>() {
			});
		} catch (IOException e) {
			LOGGER.error("toJava异常：{}", json, e);
		}
		return null;
	}

	/**
	 * JSON转Java
	 * 
	 * @param <T>   Java类型
	 * @param json  JSON
	 * @param clazz Java类型
	 * 
	 * @return Java
	 */
	public static final <T> T toJava(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			LOGGER.error("toJava异常：{}", json, e);
		}
		return null;
	}

	/**
	 * JSON转Map
	 * 
	 * @param <K>  K类型
	 * @param <V>  V类型
	 * @param json JSON
	 * 
	 * @return Map
	 */
	public static final <K, V> Map<K, V> toMap(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, new TypeReference<Map<K, V>>() {
			});
		} catch (IOException e) {
			LOGGER.error("toMap异常：{}", json, e);
		}
		return Map.of();
	}

	/**
	 * JSON转List
	 * 
	 * @param <T>  元素类型
	 * @param json JSON
	 * 
	 * @return List
	 */
	public static final <T> List<T> toList(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, new TypeReference<List<T>>() {
			});
		} catch (IOException e) {
			LOGGER.error("toList异常：{}", json, e);
		}
		return List.of();
	}

	/**
	 * 获取Mapper
	 * 
	 * @return Mapper
	 */
	public static final ObjectMapper getMapper() {
		return MAPPER;
	}

	/**
	 * 创建Mapper
	 * 
	 * @return Mapper
	 */
	public static final ObjectMapper buildMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * 创建序列化Mapper
	 * 
	 * @return 序列化Mapper
	 */
	public static final ObjectMapper buildSerializeMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		final PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class)
			.build();
		return mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL)
			.setSerializationInclusion(Include.NON_NULL);
	}

}
