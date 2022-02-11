package com.acgist.user.pojo.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.acgist.data.pojo.entity.StateEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_user", indexes = {
	@Index(name = "index_user_name", columnList = "name", unique = true)
})
public class UserEntity extends StateEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_ROLES = "roles";
	
	/**
	 * 密码
	 */
	@Column(length = 128, nullable = false)
	private String password;
	/**
	 * 角色
	 */
	@ManyToMany(fetch = FetchType.LAZY)
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
	@JsonIgnore
	private List<RoleEntity> roles;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
	
}
