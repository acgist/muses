package com.acgist.user.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.acgist.data.entity.StatusEntity;

@Entity
@Table(name = "t_user", indexes = {
	@Index(name = "index_user_name", columnList = "name", unique = true)
})
public class UserEntity extends StatusEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_MEMO = "memo";
	public static final String PROPERTY_ROLES = "roles";
	
	/**
	 * 账号
	 */
	@Column(length = 16, nullable = false)
	private String name;
	/**
	 * 密码
	 */
	@Column(length = 128, nullable = false)
	private String password;
	/**
	 * 描述
	 */
	@Column(length = 64)
	private String memo;
	/**
	 * 角色
	 */
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(
		name = "t_user_role",
		joinColumns = @JoinColumn(
			name = "user_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_user_role_user_id")
		),
		inverseJoinColumns = @JoinColumn(
			name = "role_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_user_role_role_id")
		)
	)
	private List<RoleEntity> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
	
}
