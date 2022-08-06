package ${modulePackage}${module}.model.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ${modulePackage}${module}.model.dto.${prefix}Dto;
import ${modulePackage}${module}.model.entity.${prefix}Entity;
import ${modulePackage}${module}.model.vo.${prefix}Vo;

/**
 * ${name}
 * 
 * @author ${author}
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ${prefix}Mapstruct {
	
	${prefix}Vo toVo(${prefix}Entity entity);
	
	List<${prefix}Vo> toVoList(List<${prefix}Entity> list);

	${prefix}Dto toDto(${prefix}Entity entity);

	List<${prefix}Dto> toDtoList(List<${prefix}Entity> list);
	
	${prefix}Entity toEntity(${prefix}Dto dto);
	
	List<${prefix}Entity> toEntityList(List<${prefix}Dto> list);

}
