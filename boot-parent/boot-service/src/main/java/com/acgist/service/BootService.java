package com.acgist.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.acgist.data.entity.BootEntity;
import com.acgist.data.query.FilterQuery;
import com.acgist.data.query.FilterQuery.Filter;

/**
 * BootService
 * 
 * 远程服务不要继承
 * 
 * @author acgist
 * 
 * @param <T> 类型
 */
public interface BootService<T extends BootEntity> {


	/**
	 * 列表查询
	 * 
	 * @param filters 过滤条件
	 * 
	 * @return 列表
	 */
	List<T> list(Filter ... filters);

	/**
	 * 列表查询
	 * 
	 * @param filters 过滤条件
	 * 
	 * @return 列表
	 */
	List<T> list(List<Filter> filters);
	
	/**
	 * 列表查询
	 * 
	 * @param filterQuery 过滤条件
	 * 
	 * @return 列表
	 */
	List<T> list(FilterQuery<T> filterQuery);
	
	/**
	 * 分页查询
	 * 
	 * @param filters 过滤条件
	 * @param pageable 分页信息
	 * 
	 * @return 分页
	 */
	Page<T> page(List<Filter> filters, Pageable pageable);
	
	/**
	 * 分页查询
	 * 
	 * @param filterQuery 过滤条件
	 * @param pageable 分页信息
	 * 
	 * @return 分页
	 */
	Page<T> page(FilterQuery<T> filterQuery, Pageable pageable);
	
	/**
	 * 分页查询
	 * 
	 * @param entity 类型
	 * @param filters 过滤条件
	 * @param pageable 分页信息
	 * 
	 * @return 分页
	 */
	Page<T> page(Class<T> entity, List<Filter> filters, Pageable pageable);
	
	/**
	 * 分页查询
	 * 
	 * @param entity 类型
	 * @param filterQuery 过滤条件
	 * @param pageable 分页信息
	 * 
	 * @return 分页
	 */
	Page<T> page(Class<T> entity, FilterQuery<T> filterQuery, Pageable pageable);
	
	/**
	 * 查询
	 * 
	 * @param id ID
	 * 
	 * @return 实体
	 */
	T find(Long id);
	
	/**
	 * 保存/更新
	 * 
	 * @param t 实体
	 * 
	 * @return 实体
	 */
	T save(T t);
	
	/**
	 * 删除
	 * 
	 * @param id ID
	 * 
	 * @return 是否成功
	 */
	boolean delete(Long id);
	
	/**
	 * 删除
	 * 
	 * @param t 实体
	 * 
	 * @return 是否成功
	 */
	boolean delete(T t);
	
	/**
	 * 查询所有实体
	 * 
	 * @return 所有实体列表
	 */
	List<T> findAll();
	
	/**
	 * 查询列表
	 * 
	 * @param ids ids
	 * 
	 * @return 实体列表
	 */
	List<T> findAll(Iterable<Long> ids);
	
	/**
	 * 保存列表
	 * 
	 * @param entities 实体列表
	 * 
	 * @return 实体列表
	 */
	List<T> saveAll(Iterable<T> entities);
	
	/**
	 * 删除所有实体
	 * 
	 * @return 是否成功
	 */
	boolean deleteAll();
	
	/**
	 * 删除列表
	 * 
	 * @param ids 实体ID列表
	 * 
	 * @return 是否成功
	 */
	boolean deleteAll(Iterable<Long> ids);
	
}
