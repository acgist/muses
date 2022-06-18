package ${modulePackage}${module}.model.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ${modulePackage}${module}.model.entity.${prefix}Entity;
import ${modulePackage}${module}.model.vo.${prefix}Vo;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ${prefix}Mapstruct {
	
	${prefix}Vo toVo(${prefix}Entity ${prefixLower});
	
	List<${prefix}Vo> toVos(List<${prefix}Entity> ${prefixLower});

}
