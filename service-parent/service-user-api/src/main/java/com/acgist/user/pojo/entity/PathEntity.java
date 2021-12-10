package com.acgist.user.pojo.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.acgist.data.entity.OrderEntity;

@Entity
@Table(name = "t_path", indexes = {
	@Index(name = "index_path_parent", columnList = "parent") 
})
public class PathEntity extends OrderEntity {

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
	 * GET:/user/name POST:/user/name
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
	@ManyToOne
	@JoinColumn(name = "parent", nullable = true)
	private PathEntity parent;
	/**
	 * 子集
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	@JoinColumn(name = "parent")
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
