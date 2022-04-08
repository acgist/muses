package com.acgist.mssql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.mssql.entity.Domain;

@Mapper
public interface DomainMapper {

	List<Domain> list();
	
}
