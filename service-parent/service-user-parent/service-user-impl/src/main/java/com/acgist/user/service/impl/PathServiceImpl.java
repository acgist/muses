package com.acgist.user.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

import com.acgist.service.impl.BootServiceImpl;
import com.acgist.user.dao.mapper.PathMapper;
import com.acgist.user.model.entity.PathEntity;
import com.acgist.user.service.IPathService;

@Service
public class PathServiceImpl extends BootServiceImpl<PathMapper, PathEntity> implements IPathService {

	/**
	 * 上级ID路径生成器
	 */
	private BiFunction<String, Long, String> parentIdPathBuilder = (parentIdPath, id) -> parentIdPath + "/" + id;
	
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
		final String parentIdPath = this.parentIdPathBuilder.apply(entity.getParentIdPath(), entity.getId()) + "%";
		this.baseMapper.deleteAllRolePath(entity.getId(), parentIdPath);
		this.baseMapper.deleteAll(entity.getId(), parentIdPath);
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
