package com.acgist.service.impl;

import java.util.List;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.data.pojo.entity.BootEntity;
import com.acgist.data.query.FilterQuery;
import com.acgist.data.query.FilterQuery.Filter;
import com.acgist.service.BootService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * BootServiceImpl
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
		final FilterQuery<T> query = FilterQuery.builder();
		query.merge(filters);
		return this.list(query);
	}

	@Override
	public List<T> list(FilterQuery<T> filterQuery) {
		if(filterQuery == null) {
			return this.baseMapper.selectList(Wrappers.emptyWrapper());
		}
		return this.baseMapper.selectList(filterQuery.build(this.entityClass));
	}

	@Override
	public Page<T> page(List<Filter> filters, Page<T> page) {
		final FilterQuery<T> query = FilterQuery.builder();
		query.merge(filters);
		return this.page(query, page);
	}

	@Override
	public Page<T> page(FilterQuery<T> filterQuery, Page<T> page) {
		if(filterQuery == null) {
			return this.baseMapper.selectPage(page, Wrappers.emptyWrapper());
		}
		return this.baseMapper.selectPage(page, filterQuery.build(this.entityClass));
	}
	
}
