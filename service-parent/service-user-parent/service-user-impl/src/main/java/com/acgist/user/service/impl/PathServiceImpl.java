package com.acgist.user.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.acgist.service.impl.BootServiceImpl;
import com.acgist.user.dao.mapper.PathMapper;
import com.acgist.user.model.entity.PathEntity;
import com.acgist.user.service.PathService;

@Service
public class PathServiceImpl extends BootServiceImpl<PathMapper, PathEntity> implements PathService {

	/**
	 * 上级ID路径生成器
	 */
	private BiFunction<String, Long, String> parentIdPathBuilder = (parentIdPath, id) -> Objects.toString(parentIdPath, "/") + id + "/";
	
	@Override
	public boolean save(PathEntity entity) {
		final PathEntity parent = this.getById(entity.getParentId());
		if(parent != null) {
			entity.setParentIdPath(this.parentIdPathBuilder.apply(parent.getParentIdPath(), parent.getId()));
		}
		return super.save(entity);
	}
	
	@Override
	public boolean updateById(PathEntity entity) {
		final PathEntity parent = this.getById(entity.getParentId());
		if(parent != null) {
			entity.setParentIdPath(this.parentIdPathBuilder.apply(parent.getParentIdPath(), parent.getId()));
		}
		return super.updateById(entity);
	}
	
	@Override
	public boolean removeById(Serializable id) {
		final PathEntity entity = this.getById(id);
		final String parentIdPath = this.parentIdPathBuilder.apply(entity.getParentIdPath(), entity.getId());
		final List<Serializable> deleteList = this.baseMapper.selectByPath(parentIdPath).stream().map(PathEntity::getId).collect(Collectors.toList());
		deleteList.add(id);
		this.baseMapper.deleteRolePath(deleteList);
		this.baseMapper.deleteBatchIds(deleteList);
		return true;
	}
	
	@Override
	public boolean removeByIds(Collection<?> list) {
		list.stream().map(value -> (Long) value).forEach(this::removeById);
		return true;
	}
	
	@Override
	public boolean removeBatchByIds(Collection<?> list) {
		list.stream().map(value -> (Long) value).forEach(this::removeById);
		return true;
	}
	
}
