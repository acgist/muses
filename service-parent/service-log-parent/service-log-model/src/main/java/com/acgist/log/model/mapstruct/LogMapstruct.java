package com.acgist.log.model.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.acgist.log.model.es.Log;
import com.acgist.log.model.vo.LogVo;

/**
 * 日志
 * 
 * @author acgist
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogMapstruct {

	List<LogVo> toVo(List<Log> list);
	
}
