package ${modulePackage}${module}.model.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ${modulePackage}${module}.model.entity.${prefix}Entity;
import ${modulePackage}${module}.model.vo.${prefix}Vo;

@Mapper(componentModel = "spring")
public interface ${prefix}Mapstruct {
	
	@Mapping(target = "copy", ignore = true)
	${prefix}Vo toVo(${prefix}Entity ${prefixLower});
	
	List<${prefix}Vo> toVos(List<${prefix}Entity> ${prefixLower});

}
