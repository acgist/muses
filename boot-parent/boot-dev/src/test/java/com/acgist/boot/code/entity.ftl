package com.acgist.admin.data.${modulePath}.entity;
<#if hasOtherType>

<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if hasDate>
import java.util.Date;
</#if>
</#if>

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.acgist.boot.data.entity.BootEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Data
@Entity
@Table(name = "${table}")
@TableName(value = "${table}")
@EqualsAndHashCode(callSuper = false)
public class ${prefix} extends BootEntity {

	private static final long serialVersionUID = 1L;

	<#list columns as column>
	/**
	 * ${column.comment}
	 */
	<#if column_index == 0>
	@Id
	@TableId(value = "${column.name}")
	</#if>
	@Column(name = "${column.name}")
	<#if column_index != 0>
	@TableField(value = "${column.name}")
	</#if>
	<#if column_index == 0>
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = "com.acgist.boot.data.SnowflakeIdGenerator")
	</#if>
	private ${column.type} ${column.value};
	</#list>
	
}
