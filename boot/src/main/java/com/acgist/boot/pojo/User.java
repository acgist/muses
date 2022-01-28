package com.acgist.boot.pojo;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import com.acgist.boot.CollectionUtils;
import com.acgist.boot.JSONUtils;
import com.acgist.boot.UrlUtils;

/**
 * 用户
 * 
 * @author acgist
 */
public class User implements Serializable {

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
	 * 角色
	 */
	private Set<String> roles;
	/**
	 * 地址
	 */
	private Set<String> paths;

	/**
	 * 获取网关透传用户
	 * 
	 * @return 网关透传用户
	 */
	public User currentUser() {
		final User user = new User();
		user.setId(this.id);
		user.setName(this.name);
		user.setPassword(this.password);
		return user;
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
		final String value = UrlUtils.authority(method, path);
		return this.paths.stream().anyMatch(pathValue -> pathValue.equals(value));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Set<String> getPaths() {
		return paths;
	}

	public void setPaths(Set<String> paths) {
		this.paths = paths;
	}

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

}