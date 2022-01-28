package ${modulePackage}${module}.pojo.entity;
<#if hasOtherType>

<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if hasDate>
import java.util.Date;
</#if>
</#if>
<#if jpa>

import javax.persistence.Column;
import javax.persistence.Entity;
<#if hasId>
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
</#if>
import javax.persistence.Table;
<#if hasId>

import org.hibernate.annotations.GenericGenerator;
</#if>
</#if>

import com.acgist.data.pojo.entity.BootEntity;
<#if mybatis>
import com.baomidou.mybatisplus.annotation.TableField;
<#if hasId>
import com.baomidou.mybatisplus.annotation.TableId;
</#if>
import com.baomidou.mybatisplus.annotation.TableName;
</#if>

/**
 * ${name}
 * 
 * @author ${author}
 */
<#if jpa>
@Entity
@Table(name = "${table}")
</#if>
<#if mybatis>
@TableName(value = "${table}")
</#if>
public class ${prefix} extends BootEntity {

	private static final long serialVersionUID = 1L;

	<#list columns as column>
	/**
	 * ${column.comment}
	 */
	<#if column.name == id>
	<#if jpa>
	@Id
	@Column(name = "${column.name}")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = "com.acgist.data.pojo.entity.SnowflakeGenerator")
	</#if>
	<#if mybatis>
	@TableId(value = "${column.name}")
	</#if>
	<#else>
	<#if jpa>
	@Column(name = "${column.name}")
	</#if>
	<#if mybatis>
	@TableField(value = "${column.name}")
	</#if>
	</#if>
	private ${column.type} ${column.value};
	</#list>
	<#list columns as column>
	
	public void ${column.setter}(${column.type} ${column.value}) {
		this.${column.value} = ${column.value};
	}
	
	public ${column.type} ${column.getter}() {
		return this.${column.value};
	}
	</#list>
	
}
