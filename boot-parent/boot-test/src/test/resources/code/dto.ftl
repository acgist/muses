package ${modulePackage}${module}.model.dto;
<#if typeImport?has_content>

<#list typeImport as typeValue>
import ${typeValue};
</#list>
</#if>

import com.acgist.boot.model.EntityDto;

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
public class ${prefix}Dto extends EntityDto {

	private static final long serialVersionUID = 1L;

	<#list columns as column>
	/**
	 * ${column.comment}
	 */
	private ${column.type} ${column.field};
	</#list>
	
}
