package ${modulePackage}${module}.model.vo;
<#if typeImport?has_content>

<#list typeImport as typeValue>
import ${typeValue};
</#list>
</#if>

import com.acgist.boot.model.EntityVo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Getter
@Setter
@Schema(name = "${name}")
@EqualsAndHashCode(callSuper = true)
public class ${prefix}Vo extends EntityVo {

	private static final long serialVersionUID = 1L;

	<#list columns as column>
	/**
	 * ${column.comment}
	 */
	@Schema(name = "${column.simpleComment}")
	private ${column.type} ${column.field};
	</#list>
	
}
