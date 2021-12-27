package com.acgist.user.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.acgist.data.entity.StateEntity;

@Entity
@Table(name = "t_path")
public class PathEntity extends StateEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PATH = "path";
	public static final String PROPERTY_PARENT = "parent";
	public static final String PROPERTY_CHILDREN = "children";

	/**
	 * 匹配规则
	 * 
	 * GET:/user/name
	 * POST:/user/name
	 */
	@Column(length = 128, nullable = false)
	private String path;
	/**
	 * 上级
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id", foreignKey = @ForeignKey(name = "key_path_parent_id"))
	private PathEntity parent;
	/**
	 * 子集
	 */
	@OrderBy(value = "sort ASC")
	@OneToMany(mappedBy = "parent", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	private List<PathEntity> children;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
