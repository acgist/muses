package com.acgist.boot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.data.MessageCodeException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * JSON工具
 * 
 * @author acgist
 */
public final class JSONUtils {

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
		if (Objects.isNull(object)) {
			return null;
		}
		try {
			return MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw MessageCodeException.of(e, "JSON格式失败：", object);
		}
	}

	/**
	 * JSON转Java
	 * 
	 * @param <T> Java类型
	 * 
	 * @param json JSON
	 * 
	 * @return Java
	 */
	public static final <T> T toJava(String json) {
		if (Objects.isNull(json)) {
			return null;
		}
		try {
			return MAPPER.readValue(json, new TypeReference<T>() {
			});
		} catch (IOException e) {
			throw MessageCodeException.of(e, "JSON格式错误：", json);
		}
	}

	/**
	 * JSON转Java
	 * 
	 * @param <T> Java类型
	 * 
	 * @param json JSON
	 * @param clazz Java类型
	 * 
	 * @return Java
	 */
	public static final <T> T toJava(String json, Class<T> clazz) {
		if (Objects.isNull(json) || Objects.isNull(clazz)) {
			return null;
		}
		try {
			return MAPPER.readValue(json, clazz);
		} catch (IOException e) {
			throw MessageCodeException.of(e, "JSON格式错误：", json);
		}
	}

	/**
	 * JSON转Map
	 * 
	 * @param <K> K类型
	 * @param <V> V类型
	 * 
	 * @param json JSON
	 * 
	 * @return Map
	 */
	public static final <K, V> Map<K, V> toMap(String json) {
		if (Objects.isNull(json)) {
			return Map.of();
		}
		try {
			return MAPPER.readValue(json, new TypeReference<Map<K, V>>() {
			});
		} catch (IOException e) {
			throw MessageCodeException.of(e, "JSON格式错误：", json);
		}
	}

	/**
	 * JSON转List
	 * 
	 * @param <T> 元素类型
	 * 
	 * @param json JSON
	 * 
	 * @return List
	 */
	public static final <T> List<T> toList(String json) {
		if (Objects.isNull(json)) {
			return List.of();
		}
		try {
			return MAPPER.readValue(json, new TypeReference<List<T>>() {
			});
		} catch (IOException e) {
			throw MessageCodeException.of(e, "JSON格式错误：", json);
		}
	}

	/**
	 * 创建Mapper
	 * 
	 * @return Mapper
	 */
	public static final ObjectMapper buildMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.setDateFormat(new SimpleDateFormat(MusesConfig.DATE_FORMAT))
			.registerModules(new JavaTimeModule())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setSerializationInclusion(Include.NON_NULL);
	}
	
	/**
	 * 创建WebMapper
	 * 
	 * @return Mapper
	 */
	public static final ObjectMapper buildWebMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		final SimpleModule defaultModule = new SimpleModule();
		defaultModule.addSerializer(Long.class, ToStringSerializer.instance);
		return mapper.setDateFormat(new SimpleDateFormat(MusesConfig.DATE_FORMAT))
			.registerModules(defaultModule, new JavaTimeModule())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			// 如果前台需要先是所有属性删除设置
			.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * 创建序列化Mapper
	 * 
	 * @return 序列化Mapper
	 */
	public static final ObjectMapper buildSerializeMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		// 设置类型
		final PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
			.allowIfBaseType(Object.class)
			.build();
		return mapper.setDateFormat(new SimpleDateFormat(MusesConfig.DATE_FORMAT))
			.registerModules(new JavaTimeModule())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL)
			.setSerializationInclusion(Include.NON_NULL);
	}

}
