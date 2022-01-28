package ${modulePackage}${module}.dao.repository;

import org.springframework.stereotype.Repository;

import ${modulePackage}${module}.pojo.entity.${prefix};
import com.acgist.dao.repository.BootRepository;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Repository
public interface ${prefix}Repository extends BootRepository<${prefix}> {

}
