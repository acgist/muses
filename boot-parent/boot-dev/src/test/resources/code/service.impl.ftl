package ${modulePackage}${module}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<#if mybatis>
import ${modulePackage}${module}.dao.mapper.${prefix}Mapper;
</#if>
<#if jpa>
import ${modulePackage}${module}.dao.repository.${prefix}Repository;
</#if>
import ${modulePackage}${module}.pojo.entity.${prefix};
import ${modulePackage}${module}.service.${prefix}Service;
import com.acgist.service.impl.BootServiceImpl;

@Service
public class ${prefix}ServiceImpl extends BootServiceImpl<${prefix}> implements ${prefix}Service {

	<#if !jpa && mybatis>
	@Autowired
	public ${prefix}ServiceImpl(${prefix}Mapper mapper) {
		super(mapper);
	}
	</#if>
	<#if jpa && !mybatis>
	@Autowired
	public ${prefix}ServiceImpl(${prefix}Repository repository) {
		super(repository);
	}
	</#if>
	<#if jpa && mybatis>
	@Autowired
	public ${prefix}ServiceImpl(${prefix}Mapper mapper, ${prefix}Repository repository) {
		super(mapper, repository);
	}
	</#if>

}
