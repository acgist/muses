package com.acgist.boot.model;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.acgist.boot.utils.URLUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public class User extends Model {

	private static final long serialVersionUID = 1L;

	/**
	 * 网关透传用户账号头部名称
	 */
	public static final String HEADER_CURRENT_USER = "current-user";

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 是否可用
	 */
	private boolean enabled;
	/**
	 * 角色
	 */
	private Set<String> roles;
	/**
	 * 地址
	 */
	private Set<String> paths;
	
	public User() {
	}

	public User(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * 当前用户
	 * 
	 * @param id ID
	 * @param name 名称
	 * 
	 * @return 用户
	 */
	public static final User current(Long id, String name) {
		return new User(id, name);
	}

	/**
	 * 判断是否含有角色
	 * 
	 * @param role 角色
	 * 
	 * @return 是否含有角色
	 */
	public boolean hasRole(String role) {
		if (Objects.isNull(role)) {
			return false;
		}
		if (CollectionUtils.isEmpty(this.paths)) {
			return false;
		}
		return this.roles.contains(role);
	}

	/**
	 * 判断是否含有权限
	 * 
	 * @param method 请求方法
	 * @param path 请求路径
	 * 
	 * @return 是否含有权限
	 */
	public boolean hasPath(String method, String path) {
		if (Objects.isNull(method) || Objects.isNull(path)) {
			return false;
		}
		if (CollectionUtils.isEmpty(this.paths)) {
			return false;
		}
		final String authority = URLUtils.authority(method, path);
		return this.paths.stream().anyMatch(value -> URLUtils.match(authority, value));
	}

}