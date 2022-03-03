package com.acgist.service.impl;

import java.util.List;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.data.entity.BootEntity;
import com.acgist.data.query.FilterQuery;
import com.acgist.data.query.FilterQuery.Filter;
import com.acgist.service.BootService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 基础Service实现
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
public abstract class BootServiceImpl<M extends BootMapper<T>, T extends BootEntity> extends ServiceImpl<M, T> implements BootService<T> {

	@Override
	public List<T> list(Filter ... filters) {
		return this.list(List.of(filters));
	}

	@Override
	public List<T> list(List<Filter> filters) {
		return this.list(FilterQuery.builder().merge(filters));
	}

	@Override
	public List<T> list(FilterQuery filterQuery) {
		if(filterQuery == null) {
			return this.baseMapper.selectList(Wrappers.emptyWrapper());
		}
		return this.baseMapper.selectList(filterQuery.build(this.entityClass));
	}

	@Override
	public Page<T> page(FilterQuery filterQuery, Page<T> page) {
		if(filterQuery == null) {
			return this.baseMapper.selectPage(page, Wrappers.emptyWrapper());
		}
		return this.baseMapper.selectPage(page, filterQuery.build(this.entityClass));
	}
	
}
