package com.acgist.user.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "t_path", indexes = {
	@Index(name = "index_path_parent", columnList = "parent")
})
public class PathEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@Column(length = 20, nullable = false)
	private String name;
	/**
	 * 匹配规则
	 * 
	 * POST:/user
	 * POST:/user/.*
	 */
	@Column(length = 50, nullable = false)
	private String pattern;
	/**
	 * 描述
	 */
	@Column(length = 100)
	private String memo;
	/**
	 * 父级菜单
	 */
	@Column(length = 32)
	private String parent;
	/**
	 * 排序
	 */
	private Short sort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Short getSort() {
		return sort;
	}

	public void setSort(Short sort) {
		this.sort = sort;
	}

}
