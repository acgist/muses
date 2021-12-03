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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_user", indexes = {
	@Index(name = "index_user_name", columnList = "name", unique = true)
})
public class UserEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_MEMO = "memo";
	public static final String PROPERTY_ROLES = "roles";
	
	/**
	 * 账号
	 */
	@Column(length = 20, nullable = false)
	private String name;
	/**
	 * 密码
	 */
	@Column(length = 50, nullable = false)
	private String password;
	/**
	 * 描述
	 */
	@Column(length = 100)
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
}
