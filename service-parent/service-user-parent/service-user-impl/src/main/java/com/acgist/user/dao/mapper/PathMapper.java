package com.acgist.user.dao.mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.dao.mapper.BootMapper;
import com.acgist.user.model.entity.PathEntity;

/**
 * 权限Mapper
 * 
 * @author acgist
 */
@Mapper
public interface PathMapper extends BootMapper<PathEntity> {
	
	/**
	 * 查询当前权限所有下级权限
	 * 
	 * @param parentIdPath 上级ID路径
	 * 
	 * @return 当前权限以及下级权限
	 */
	List<PathEntity> selectByPath(String parentIdPath);
	
	/**
	 * 删除权限角色关联
	 * 
	 * @param deleteList 删除权限ID列表
	 * 
	 * @return 删除数量
	 */
	int deleteRolePath(List<Serializable> deleteList);
	
	/**
	 * 根据角色ID查询权限列表
	 * 
	 * @param id 角色ID
	 * 
	 * @return 权限列表
	 */
	List<PathEntity> selectByRole(Long id);

	/**
	 * 根据用户名称查询权限列表
	 * 
	 * @param name 用户名称
	 * 
	 * @return 权限列表
	 */
	List<PathEntity> selectByUser(String name);

	/**
	 * 树化权限列表
	 * 
	 * @param list 权限列表
	 * 
	 * @return 树型权限列表
	 */
	default List<PathEntity> tree(List<PathEntity> list) {
		// 树化
		list.forEach(path -> {
			final Optional<PathEntity> optional = list.stream().filter(value -> value.getId().equals(path.getParentId())).findFirst();
			if(optional.isPresent()) {
				final PathEntity parent = optional.get();
				if (parent.getChildren() == null) {
					parent.setChildren(new ArrayList<>());
				}
				// 添加重复判断：MyBatisPlus开启事务就会使用缓存，重复查询就会导致权限重复。
				if(!path.getChildren().contains(path)) {
					parent.getChildren().add(path);
				}
			}
		});
		// 排序下级菜单
		list.forEach(path -> {
			if(path.getChildren() == null) {
				path.setChildren(new ArrayList<>());
			} else {
				path.getChildren().sort(Comparator.comparing(entity -> entity.getSorted() == null ? 0 : entity.getSorted()));
			}
		});
		// 返回顶级菜单
		return list.stream()
			.distinct()
			.filter(path -> path.getParentId() == null)
			.sorted(Comparator.comparing(entity -> entity.getSorted() == null ? 0 : entity.getSorted()))
			.collect(Collectors.toList());
	}
	
}
