package com.acgist.oauth2.model;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

/**
 * 授权信息
 * 
 * @author acgist
 */
@Getter
@Setter
public class Principal extends User {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 角色
	 */
	private Set<String> roles;

	public Principal(
		Long id, Set<String> roles, String username, String password,
		boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
		Collection<? extends GrantedAuthority> authorities
	) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.roles = roles;
	}

}
