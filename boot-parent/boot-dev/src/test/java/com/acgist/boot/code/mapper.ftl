package com.acgist.admin.${modulePath}.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.admin.data.${modulePath}.entity.${prefix};
import com.acgist.boot.dao.mapper.BootMapper;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Mapper
public interface ${prefix}Mapper extends BootMapper<${prefix}> {

}
