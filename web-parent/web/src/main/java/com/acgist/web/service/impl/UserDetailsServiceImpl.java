package com.acgist.web.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.acgist.user.api.IUserService;

/**
 * 查询用户
 * 
 * @author acgist
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@DubboReference
	private IUserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final com.acgist.boot.model.User user = this.userService.selectByName(username);
		if(user == null) {
			throw new UsernameNotFoundException("用户无效");
		}
		return new User(user.getName(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getPaths().toArray(String[]::new)));
	}

}
