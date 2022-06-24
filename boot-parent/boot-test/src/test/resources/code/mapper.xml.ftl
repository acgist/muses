<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${modulePackage}${module}.dao.mapper.${prefix}Mapper">

	<resultMap id="${prefix}Entity" type="${modulePackage}${module}.model.entity.${prefix}Entity">
		<id column="id" property="id" />
		<#list columns as column>
		<result column="${column.column}" property="${column.field}" />
		</#list>
		<result column="create_date" property="createDate" />
		<result column="modify_date" property="modifyDate" />
	</resultMap>
	
	<resultMap id="${prefix}Vo" type="${modulePackage}${module}.model.vo.${prefix}Vo" extends="${prefix}Entity">
	</resultMap>
	
	<resultMap id="${prefix}Dto" type="${modulePackage}${module}.model.dto.${prefix}Dto" extends="${prefix}Entity">
	</resultMap>
	
</mapper>
