package com.acgist.admin.${module}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.admin.data.${module}.entity.${prefix};
import com.acgist.admin.${module}.dao.repository.${prefix}Repository;
import com.acgist.admin.${module}.service.${prefix}Service;
import com.acgist.service.impl.BootServiceImpl;

@Service
public class ${prefix}ServiceImpl extends BootServiceImpl<${prefix}> implements ${prefix}Service {

	@Autowired
	public ${prefix}ServiceImpl(${prefix}Repository repository) {
		super(repository);
	}

}
