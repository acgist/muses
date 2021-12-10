package com.acgist.user.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.acgist.data.entity.DataEntity;

@Entity
@Table(name = "t_path", indexes = {
	@Index(name = "index_path_parent_id", columnList = "parent_id") 
})
public class PathEntity extends DataEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_PATH = "path";
	public static final String PROPERTY_MEMO = "memo";
	public static final String PROPERTY_PARENT = "parent";

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
	 * 排序
	 */
	@Column
	private Short sort;
	/**
	 * 上级
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private PathEntity parent;
	/**
	 * 子集
	 */
	@OrderBy(value = "sort ASC")
	@OneToMany(mappedBy = "parent", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	private List<PathEntity> children;

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

	public Short getSort() {
		return sort;
	}

	public void setSort(Short sort) {
		this.sort = sort;
	}

	public PathEntity getParent() {
		return parent;
	}

	public void setParent(PathEntity parent) {
		this.parent = parent;
	}

	public List<PathEntity> getChildren() {
		return children;
	}

	public void setChildren(List<PathEntity> children) {
		this.children = children;
	}

}
