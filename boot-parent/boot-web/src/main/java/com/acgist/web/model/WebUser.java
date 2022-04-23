package com.acgist.web.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * WebUser
 * 
 * @author acgist
 */
public class WebUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private final Long id;
	/**
	 * 名称
	 */
	private final String username;
	/**
	 * 密码
	 */
	private final String password;
	/**
	 * 是否可用
	 */
	private final boolean enabled;
	/**
	 * 没有被锁
	 */
	private final boolean accountNonLocked;
	/**
	 * 没有过期
	 */
	private final boolean accountNonExpired;
	/**
	 * 没有过期
	 */
	private final boolean credentialsNonExpired;
	/**
	 * 拥有权限
	 */
	private final Set<GrantedAuthority> authorities;

	public WebUser(
		Long id, String username, String password,
		Collection<? extends GrantedAuthority> authorities
	) {
		this(id, username, password, true, true, true, true, authorities);
	}
	
	public WebUser(
		Long id, String username, String password, boolean enabled,
		boolean accountNonLocked, boolean accountNonExpired, boolean credentialsNonExpired,
		Collection<? extends GrantedAuthority> authorities
	) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonLocked = accountNonLocked;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.authorities = Collections.unmodifiableSet(new HashSet<>(authorities));
	}
	
	public Long getId() {
		return this.id;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
}
