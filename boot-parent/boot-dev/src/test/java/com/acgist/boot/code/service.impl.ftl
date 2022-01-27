package com.acgist.admin.${modulePath}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.admin.data.${modulePath}.entity.${prefix};
import com.acgist.admin.${modulePath}.dao.repository.${prefix}Repository;
import com.acgist.admin.${modulePath}.service.${prefix}Service;
import com.acgist.boot.service.impl.BootServiceImpl;

@Service
public class ${prefix}ServiceImpl extends BootServiceImpl<${prefix}> implements ${prefix}Service {

	@Autowired
	public ${prefix}ServiceImpl(${prefix}Repository repository) {
		super(repository);
	}

}
