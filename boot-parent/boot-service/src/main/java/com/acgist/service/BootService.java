package com.acgist.service;

import java.util.List;

import com.acgist.model.entity.BootEntity;
import com.acgist.model.query.FilterQuery;
import com.acgist.model.query.FilterQuery.Filter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础Service
 * 
 * 远程服务不要继承
 * 
 * @author acgist
 * 
 * @param <T> 类型
 */
public interface BootService<T extends BootEntity> extends IService<T> {


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
	List<T> list(FilterQuery filterQuery);
	
	/**
	 * 分页查询
	 * 
	 * @param filterQuery 过滤条件
	 * @param page 分页信息
	 * 
	 * @return 分页
	 */
	Page<T> page(FilterQuery filterQuery, Page<T> page);
	
}
