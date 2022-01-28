package com.acgist.admin.data.${module}.entity;
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

import com.acgist.data.pojo.entity.BootEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
	@Column(name = "${column.name}")
	@TableId(value = "${column.name}")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = "com.acgist.data.pojo.entity.SnowflakeGenerator")
	<#else>
	@Column(name = "${column.name}")
	@TableField(value = "${column.name}")
	</#if>
	private ${column.type} ${column.value};
	</#list>
	
}
