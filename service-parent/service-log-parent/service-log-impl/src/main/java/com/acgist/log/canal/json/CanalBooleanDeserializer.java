package com.acgist.log.canal.json;

import java.io.IOException;

import com.alibaba.nacos.common.utils.Objects;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Boolean扩展
 * 
 * @author acgist
 */
@JacksonStdImpl
public class CanalBooleanDeserializer extends StdDeserializer<Boolean> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 适配对象
	 */
	private BooleanDeserializer deserializer = new BooleanDeserializer(Boolean.class, Boolean.FALSE);

	public CanalBooleanDeserializer() {
		super(Boolean.class);
	}

	@Override
	public Boolean deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		if(parser.currentTokenId() == JsonTokenId.ID_STRING) {
			final String value = parser.getValueAsString();
			if(value.length() == 1) {
				return !Objects.equals(value, "0");
			}
		}
		return this.deserializer.deserialize(parser, context);
	}

}
