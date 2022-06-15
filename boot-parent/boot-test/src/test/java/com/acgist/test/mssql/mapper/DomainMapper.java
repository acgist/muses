package com.acgist.test.mssql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.test.mssql.entity.Domain;

@Mapper
public interface DomainMapper {

	List<Domain> list();
	
}
