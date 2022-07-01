package com.acgist.user.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.user.api.IRoleService;
import com.acgist.user.dao.mapper.PathMapper;
import com.acgist.user.dao.mapper.RoleMapper;
import com.acgist.user.model.dto.RoleDto;
import com.acgist.user.model.entity.PathEntity;
import com.acgist.user.model.entity.RoleEntity;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PathMapper pathMapper;
	
	@Override
	public List<RoleDto> all() {
		return this.roleMapper.selectList(Wrappers.lambdaQuery(RoleEntity.class)).stream()
			.map(value -> {
				final RoleDto role = new RoleDto();
				role.setName(value.getName());
				role.setPaths(this.pathMapper.selectByRole(value.getId()).stream().filter(PathEntity::getEnabled).map(PathEntity::getPath).collect(Collectors.toList()));
				return role;
			}).collect(Collectors.toList());
	}

}
