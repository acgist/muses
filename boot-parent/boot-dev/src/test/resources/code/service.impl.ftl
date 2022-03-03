package ${modulePackage}${module}.service.impl;

import org.springframework.stereotype.Service;

import ${modulePackage}${module}.dao.mapper.${prefix}Mapper;
import ${modulePackage}${module}.data.entity.${prefix}Entity;
import ${modulePackage}${module}.service.${prefix}Service;
import com.acgist.service.impl.BootServiceImpl;

@Service
public class ${prefix}ServiceImpl extends BootServiceImpl<${prefix}Mapper, ${prefix}Entity> implements ${prefix}Service {

}
