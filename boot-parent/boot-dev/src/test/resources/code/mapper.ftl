package ${modulePackage}${module}.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import ${modulePackage}${module}.pojo.entity.${prefix};
import com.acgist.dao.mapper.BootMapper;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Mapper
public interface ${prefix}Mapper extends BootMapper<${prefix}> {

}
