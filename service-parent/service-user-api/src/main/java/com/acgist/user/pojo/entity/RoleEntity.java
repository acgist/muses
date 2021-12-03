package com.acgist.user.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_role")
public class RoleEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_PERMISSIONS = "permissions";
	
	/**
	 * 名称
	 */
	@Column(length = 20, nullable = false)
	private String name;
	/**
	 * 描述
	 */
	@Column(length = 100)
	private String memo;
	/**
	 * 权限
	 */
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(
	name = "ts_role_path",
	joinColumns = @JoinColumn(
			name = "role_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_role_path_role_id")
			),
	inverseJoinColumns = @JoinColumn(
			name = "path_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_role_path_path_id")
			)
	)
	private List<PathEntity> paths;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<PermissionEntity> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionEntity> permissions) {
		this.permissions = permissions;
	}
	
}
