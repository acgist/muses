package com.acgist.model.es;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.acgist.boot.model.Model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * ES文档
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public abstract class BootDocument extends Model {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	@Id
	private Long id;
	/**
	 * 创建时间
	 */
	@Field(type = FieldType.Date)
	private LocalDateTime createDate;
	/**
	 * 修改时间
	 */
	@Field(type = FieldType.Date)
	private LocalDateTime modifyDate;
	
}
