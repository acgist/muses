package com.acgist.www.transfer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 枚举翻译
 * 
 * @author acgist
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@JsonSerialize(using = TransferSerializer.class, nullsUsing = TransferSerializer.class)
@JacksonAnnotationsInside
public @interface Transfer {

	/**
	 * @return 枚举组
	 */
	String group();
	
}
