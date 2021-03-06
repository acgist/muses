package com.acgist.www.transfer;

import java.io.IOException;
import java.util.Map;

import com.acgist.boot.service.CacheService;
import com.acgist.boot.utils.SpringUtils;
import com.acgist.service.TransferService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * 枚举翻译
 * 
 * 原始字段后面添加`Value`作为翻译字段
 * 
 * 如果使用弱引用作缓存key必须new一个String
 * 
 * @author acgist
 */
public class TransferSerializer extends JsonSerializer<Object> implements ContextualSerializer {
	
	/**
	 * 枚举分组
	 */
	private final String group;
	/**
	 * 缓存
	 */
	private final CacheService cacheService;
	/**
	 * 枚举翻译服务
	 */
	private final TransferService transferService;
	
	public TransferSerializer() {
		this(null, null, null);
	}
	
	public TransferSerializer(String group, CacheService cacheService, TransferService transferService) {
		this.group = group;
		this.cacheService = cacheService;
		this.transferService = transferService;
	}

	@Override
	public JsonSerializer<Object> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
		if(property == null) {
			return provider.findNullValueSerializer(property);
		}
		// 支持String类型：Objects.equals(property.getType().getRawClass(), String.class)
		Transfer transfer = property.getAnnotation(Transfer.class);
		if(transfer == null) {
			transfer = property.getContextAnnotation(Transfer.class);
		}
		if(transfer != null) {
			return new TransferSerializer(
				transfer.group(),
				SpringUtils.getBeanNullable(CacheService.class),
				SpringUtils.getBeanNullable(TransferService.class)
			);
		}
		return provider.findValueSerializer(property.getType(), property);
	}

	@Override
	public void serialize(final Object object, JsonGenerator generator, SerializerProvider provider) throws IOException {
		final String value = object == null ? null : object.toString();
		// 原始字段输出
		generator.writeString(value);
		// 翻译字段输出
		final String fieldName = generator.getOutputContext().getCurrentName() + "Value";
		generator.writeStringField(fieldName, this.transferMap().getOrDefault(value, value));
	}
	
	/**
	 * @return 分组翻译
	 */
	private Map<String, String> transferMap() {
		if(this.cacheService == null || this.transferService == null) {
			return Map.of();
		}
		Map<String, String> transferMap = this.cacheService.cache(CacheService.CACHE_TRANSFER, this.group);
		if(transferMap == null) {
			transferMap = this.transferService.select(this.group);
			this.cacheService.cache(CacheService.CACHE_TRANSFER, this.group, transferMap);
		}
		return transferMap;
	}

}
