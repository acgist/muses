package com.acgist.model.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.acgist.boot.model.Model;
import com.acgist.boot.utils.FormatStyle;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据库实体类
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public abstract class BootEntity extends Model {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_CREATE_DATE = "createDate";
	public static final String PROPERTY_MODIFY_DATE = "modifyDate";
	
	/**
	 * ID
	 */
	@TableId(value = "id")
	private Long id;
	/**
	 * 创建时间
	 */
//	@Column(name = "created_by", updatable = false)
	@TableField(value = "create_date", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
	@JsonFormat(pattern = FormatStyle.YYYY_MM_DD_HH24_MM_SS)
	@DateTimeFormat(pattern = FormatStyle.YYYY_MM_DD_HH24_MM_SS)
	private LocalDateTime createDate;
	/**
	 * 修改时间
	 */
//	@Column(name = "created_by", updatable = false)
	@TableField(value = "modify_date", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = FormatStyle.YYYY_MM_DD_HH24_MM_SS)
	@DateTimeFormat(pattern = FormatStyle.YYYY_MM_DD_HH24_MM_SS)
	private LocalDateTime modifyDate;

}
