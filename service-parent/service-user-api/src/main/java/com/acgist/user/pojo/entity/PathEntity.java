package com.acgist.user.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.acgist.data.entity.DataEntity;

@Entity
@Table(name = "t_path", indexes = { @Index(name = "index_path_parent", columnList = "parent") })
public class PathEntity extends DataEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_PATH = "path";
	public static final String PROPERTY_MEMO = "memo";
	public static final String PROPERTY_PARENT = "parent";
	public static final String PROPERTY_SORT = "sort";

	/**
	 * 名称
	 */
	@Column(length = 32, nullable = false)
	private String name;
	/**
	 * 匹配规则
	 * 
	 * GET:/user/name
	 * POST:/user/name
	 */
	@Column(length = 128, nullable = false)
	private String path;
	/**
	 * 描述
	 */
	@Column(length = 64)
	private String memo;
	/**
	 * 上级
	 */
	@Column
	private Long parent;
	/**
	 * 排序
	 */
	@Column
	private Short sort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Short getSort() {
		return sort;
	}

	public void setSort(Short sort) {
		this.sort = sort;
	}

}
