package com.acgist.boot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

public final class JSONUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

	private JSONUtils() {
	}
	
	private static final ObjectMapper MAPPER = buildMapper();

	public static final String toJSON(Object object) {
		if (object == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOGGER.error("toJson：{}", object, e);
		}
		return null;
	}

	public static final <K, V> Map<K, V> toMap(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, new TypeReference<Map<K, V>>() {
			});
		} catch (IOException e) {
			LOGGER.error("toMap：{}", json, e);
		}
		return null;
	}

	public static final <T> List<T> toList(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, new TypeReference<List<T>>() {
			});
		} catch (IOException e) {
			LOGGER.error("toList：{}", json, e);
		}
		return null;
	}

	public static final <T> T toJava(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, new TypeReference<T>() {
			});
		} catch (IOException e) {
			LOGGER.error("toJava：{}", json, e);
		}
		return null;
	}
	
	public static final <T> T toJava(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			LOGGER.error("toJava：{}", json, e);
		}
		return null;
	}

	public static final ObjectMapper getMapper() {
		return MAPPER;
	}

	public static final ObjectMapper buildMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
			.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
	
	public static final ObjectMapper buildSerializeMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		final PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator
			.builder()
			.allowIfBaseType(Object.class)
			.build();
		mapper
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
			.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL)
			.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}

}
