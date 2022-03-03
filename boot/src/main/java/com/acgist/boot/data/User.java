package com.acgist.boot.data;

import java.util.Objects;
import java.util.Set;

import com.acgist.boot.CollectionUtils;
import com.acgist.boot.UrlUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户
 * 
 * @author acgist
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "id")
public class User extends PojoCopy {

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

}