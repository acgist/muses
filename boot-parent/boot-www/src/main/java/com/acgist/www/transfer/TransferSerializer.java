package com.acgist.www.transfer;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.acgist.boot.SpringUtils;
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
 * @author acgist
 */
public class TransferSerializer extends JsonSerializer<String> implements ContextualSerializer {
	
	/**
	 * 枚举分组
	 */
	private final String group;
	/**
	 * 枚举翻译服务
	 */
	private final TransferService transferService;
	
	public TransferSerializer() {
		this(null, null);
	}
	
	public TransferSerializer(String group, TransferService transferService) {
		this.group = group;
		this.transferService = transferService;
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
		if(property == null) {
			return provider.findNullValueSerializer(property);
		}
		// 支持String类型
		if(Objects.equals(property.getType().getRawClass(), String.class)) {
			Transfer transfer = property.getAnnotation(Transfer.class);
			if(transfer == null) {
				transfer = property.getContextAnnotation(Transfer.class);
			}
			if(transfer != null) {
				return new TransferSerializer(transfer.group(), SpringUtils.getBean(TransferService.class));
			}
		}
		return provider.findValueSerializer(property.getType(), property);
	}

	@Override
	public void serialize(String value, JsonGenerator generator, SerializerProvider provider) throws IOException {
		// 原始字段输出
		generator.writeString(value);
		final String fieldName = generator.getOutputContext().getCurrentName() + "Value";
		String fieldValue = value;
		if(this.transferService != null) {
			// 翻译字段输出
			final Map<String, String> map = this.transferService.select(this.group);
			fieldValue = map == null ? value : map.getOrDefault(value, value);
		}
		generator.writeStringField(fieldName, fieldValue);
	}

}
