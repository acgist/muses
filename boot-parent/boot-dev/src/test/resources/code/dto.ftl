package ${modulePackage}${module}.model.dto;
<#if hasOtherType>

<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if hasDate>
import java.util.Date;
</#if>
</#if>

import com.acgist.boot.model.BootDto;

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
public class ${prefix}Dto extends BootDto {

	private static final long serialVersionUID = 1L;

	<#list columns as column>
	/**
	 * ${column.comment}
	 */
	private ${column.type} ${column.value};
	</#list>
	
}
