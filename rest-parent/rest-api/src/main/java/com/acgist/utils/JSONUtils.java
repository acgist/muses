package com.acgist.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>工具 - JSON（Jackson）</p>
 * 
 * @author acgist
 */
public final class JSONUtils {

	public static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);
	
	private JSONUtils() {
	}
	
	private static final ObjectMapper MAPPER;
	
	static {
		final ObjectMapper mapper = new ObjectMapper();
		// 不序列化null值，使用注解：@JsonInclude(Include.NON_NULL)
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// 未知属性不反序列化，使用注解：@JsonIgnoreProperties(ignoreUnknown = true)
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MAPPER = mapper;
	}
	
	/**
	 * <p>JSON序列化</p>
	 * 
	 * @param object Java对象
	 * 
	 * @return JSON字符串
	 */
	public static final String serialize(Object object) {
		if(object == null) {
			return null;
		}
		try {
			return MAPPER.writeValueAsString(object);
		} catch (Exception e) {
			LOGGER.error("JSON序列化异常：{}", object, e);
		}
		return null;
	}
	
	/**
	 * <p>JSON反序列化</p>
	 * 
	 * @param <T> 泛型
	 * 
	 * @param json JSON字符串
	 * @param clazz 泛型类型
	 * 
	 * @return Java对象
	 */
	public static final <T> T unserialize(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, clazz);
		} catch (IOException e) {
			LOGGER.error("JSON反序列化异常：{}", json, e);
		}
		return null;
	}
	
	/**
	 * <p>将JSON字符串转Map对象</p>
	 * 
	 * @param json JSON字符串
	 * 
	 * @return Map对象
	 */
	public static final <K, V> Map<K, V> toMap(String json) {
		if (json == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, new TypeReference<Map<K, V>>() {});
		} catch (IOException e) {
			LOGGER.error("JSON字符串转Map对象异常：{}", json, e);
		}
		return null;
	}
	
	/**
	 * <p>将JSON字符串转List对象</p>
	 * 
	 * @param <T> 类型
	 * 
	 * @param json JSON字符串
	 * 
	 * @return List对象
	 */
	public static final <T> List<T> toList(String json) {
		if (json == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, new TypeReference<List<T>>() {});
		} catch (IOException e) {
			LOGGER.error("JSON字符串转List对象异常：{}", json, e);
		}
		return null;
	}
	
}
