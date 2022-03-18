package com.acgist.model.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.model.PojoCopy;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
public abstract class BootEntity extends PojoCopy {

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
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	@JsonFormat(pattern = MusesConfig.DATE_TIME_FORMAT)
	@DateTimeFormat(pattern = MusesConfig.DATE_TIME_FORMAT)
	private Date createDate;
	/**
	 * 修改时间
	 */
	@TableField(value = "modify_date", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = MusesConfig.DATE_TIME_FORMAT)
	@DateTimeFormat(pattern = MusesConfig.DATE_TIME_FORMAT)
	private Date modifyDate;

}
