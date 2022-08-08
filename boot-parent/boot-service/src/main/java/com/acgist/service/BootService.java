package com.acgist.service;

import java.util.List;
import java.util.function.Function;

import com.acgist.model.entity.BootEntity;
import com.acgist.model.query.FilterQuery;
import com.acgist.model.query.FilterQuery.Filter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
	 * @return 所有实体
	 */
	default List<T> findAll() {
		return this.list(Wrappers.emptyWrapper());
	}

	/**
	 * 列表查询
	 * 
	 * @param filters 过滤条件
	 * 
	 * @return 列表
	 */
	default List<T> list(Filter ... filters) {
		return this.list(List.of(filters));
	}

	/**
	 * 列表查询
	 * 
	 * @param filters 过滤条件
	 * 
	 * @return 列表
	 */
	default List<T> list(List<Filter> filters) {
		return this.list(FilterQuery.builder().merge(filters));
	}
	
	/**
	 * 列表查询
	 * 
	 * @param filterQuery 过滤条件
	 * 
	 * @return 列表
	 */
	default List<T> list(FilterQuery filterQuery) {
		if(filterQuery == null) {
			return this.getBaseMapper().selectList(Wrappers.emptyWrapper());
		}
		return this.getBaseMapper().selectList(filterQuery.build(this.getEntityClass()));
	}
	
	/**
	 * 分页查询
	 * 
	 * @param filterQuery 过滤条件
	 * 
	 * @return 分页
	 */
	default Page<T> page(FilterQuery filterQuery) {
		return this.page(filterQuery, filterQuery.buildPage());
	}
	
	/**
	 * 分页查询
	 * 
	 * @param filterQuery 过滤条件
	 * @param page 分页信息
	 * 
	 * @return 分页
	 */
	default Page<T> page(FilterQuery filterQuery, Page<T> page) {
		if(filterQuery == null) {
			return this.getBaseMapper().selectPage(page, Wrappers.emptyWrapper());
		}
		return this.getBaseMapper().selectPage(page, filterQuery.build(this.getEntityClass()));
	}
	
	/**
	 * Entity分页结果转为Vo分页结果
	 * 
	 * @param <E> Entity类型
	 * @param <V> Vo类型
	 * 
	 * @param page Entity分页结果
	 * @param converter 转换器（推荐使用Mapstruct）
	 * 
	 * @return Vo分页结果
	 */
	default <E, V> Page<V> toVo(Page<E> page, Function<List<E>, List<V>> converter) {
		final Page<V> voPage = new Page<V>(page.getCurrent(), page.getSize(), page.getTotal());
		voPage.setRecords(converter.apply(page.getRecords()));
		return voPage;
	}
	
}
