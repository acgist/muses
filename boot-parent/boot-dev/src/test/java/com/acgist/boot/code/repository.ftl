package com.acgist.admin.${modulePath}.dao.repository;

import org.springframework.stereotype.Repository;

import com.acgist.admin.data.${modulePath}.entity.${prefix};
import com.acgist.boot.dao.repository.BootRepository;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Repository
public interface ${prefix}Repository extends BootRepository<${prefix}> {

}
