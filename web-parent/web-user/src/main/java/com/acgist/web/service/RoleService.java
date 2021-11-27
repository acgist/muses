package com.acgist.web.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		return authentication != null;
	}
	
}