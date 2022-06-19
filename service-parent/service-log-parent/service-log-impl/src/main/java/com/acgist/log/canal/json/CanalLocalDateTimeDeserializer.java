package com.acgist.log.canal.json;

import java.io.IOException;
import java.time.LocalDateTime;

import com.acgist.boot.utils.FormatStyle.DateTimeStyle;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

/**
 * 日期扩展
 * 
 * @author acgist
 */
@JacksonStdImpl
public class CanalLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 适配对象
	 */
	private LocalDateTimeDeserializer deserializer = new LocalDateTimeDeserializer(DateTimeStyle.YYYY_MM_DD_HH24_MM_SS.getDateTimeFormatter());
	
	public CanalLocalDateTimeDeserializer() {
		super(LocalDateTime.class);
	}
	
	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		if(parser.currentTokenId() == JsonTokenId.ID_STRING) {
			// 2022-12-12 12:12:12.000000
			final String value = parser.getValueAsString();
			final int index = value.indexOf('.');
			if(index >= 0) {
				return LocalDateTime.parse(value.subSequence(0, index), DateTimeStyle.YYYY_MM_DD_HH24_MM_SS.getDateTimeFormatter());
			}
		}
		return this.deserializer.deserialize(parser, context);
	}

}
