package com.acgist.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.dao.repository.BootRepository;
import com.acgist.data.pojo.entity.BootEntity;
import com.acgist.data.query.FilterQuery;
import com.acgist.data.query.FilterQuery.Filter;
import com.acgist.service.BootService;

/**
 * BootServiceImpl
 * 
 * @author acgist
 *
 * @param <T> 类型
 */
public abstract class BootServiceImpl<T extends BootEntity> implements BootService<T> {

	/**
	 * Mapper
	 */
	protected BootMapper<T> mapper;
	/**
	 * Repository
	 */
	protected BootRepository<T> repository;

	/**
	 * 可以通过@Autowired注解构造函数注入
	 * 
	 * @param repository repository
	 */
	public BootServiceImpl(BootRepository<T> repository) {
		this.repository = repository;
	}
	
	/**
	 * 可以通过@Autowired注解构造函数注入
	 * 
	 * @param mapper mapper
	 * @param repository repository
	 */
	public BootServiceImpl(BootMapper<T> mapper, BootRepository<T> repository) {
		this.mapper = mapper;
		this.repository = repository;
	}

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
			return this.repository.findAll();
		}
		return this.repository.findAll(filterQuery.build());
	}

	@Override
	public Page<T> page(List<Filter> filters, Pageable pageable) {
		final FilterQuery<T> query = FilterQuery.builder();
		query.merge(filters);
		return this.page(query, pageable);
	}

	@Override
	public Page<T> page(FilterQuery<T> filterQuery, Pageable pageable) {
		if(filterQuery == null) {
			return this.repository.findAll(pageable);
		}
		return this.repository.findAll(filterQuery.build(), pageable);
	}
	
	@Override
	public Page<T> page(Class<T> entity, List<Filter> filters, Pageable pageable) {
		final FilterQuery<T> query = FilterQuery.builder();
		query.merge(filters);
		return this.page(entity, query, pageable);
	}

	@Override
	public Page<T> page(Class<T> entity, FilterQuery<T> filterQuery, Pageable pageable) {
		final com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>(pageable.getPageNumber(), pageable.getPageSize());
		final com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> result;
		if(filterQuery == null) {
			result = this.mapper.selectPage(page, null);
		} else {
			result = this.mapper.selectPage(page, filterQuery.build(entity));
		}
		return new PageImpl<>(result.getRecords(), pageable, result.getTotal());
	}

	@Override
	public T find(Long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public T save(T t) {
		return this.repository.saveAndFlush(t);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public boolean delete(Long id) {
		this.repository.deleteById(id);
		return true;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public boolean delete(T t) {
		this.repository.delete(t);
		return true;
	}
	
	@Override
	public List<T> findAll() {
		return this.repository.findAll();
	}
	
	@Override
	public List<T> findAll(Iterable<Long> ids) {
		return this.repository.findAllById(ids);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public List<T> saveAll(Iterable<T> entities) {
		return this.repository.saveAll(entities);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public boolean deleteAll() {
		// 不要实现
		return false;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public boolean deleteAll(Iterable<Long> ids) {
		this.findAll(ids).forEach(this::delete);
		return true;
	}
	
}
