package ${modulePackage}${module}.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import ${modulePackage}${module}.model.entity.${prefix}Entity;
import com.acgist.dao.mapper.BootMapper;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Mapper
public interface ${prefix}Mapper extends BootMapper<${prefix}Entity> {

}
