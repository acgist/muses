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
public class ${prefix}ServiceImpl extends BootServiceImpl<${prefix}Mapper, ${prefix}Entity> implements ${prefix}Service {

}
