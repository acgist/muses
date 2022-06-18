package ${modulePackage}${module}.model.entity;
<#if typeImport?has_content>

<#list typeImport as typeValue>
import ${typeValue};
</#list>
</#if>

import com.acgist.model.entity.BootEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName(value = "${table}")
public class ${prefix}Entity extends BootEntity {

	private static final long serialVersionUID = 1L;

	<#list columns as column>
	/**
	 * ${column.comment}
	 */
	@TableField(value = "${column.column}")
	private ${column.type} ${column.field};
	</#list>
	
}
