package com.acgist.www.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.acgist.boot.service.RsaService;

/**
 * RSA加密
 * 
 * @author acgist
 */
public class RsaPasswordEncoder implements PasswordEncoder {

	/**
	 * 代理
	 */
	private BCryptPasswordEncoder proxy = new BCryptPasswordEncoder();
	
	@Autowired
	private RsaService rsaService;
	
	@Override
	public String encode(CharSequence rawPassword) {
		return this.proxy.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String password = rawPassword.toString().strip();
		// 明文校验
		boolean success = this.proxy.matches(password, encodedPassword);
		// 解密校验
		if(!success && password.length() > 64) {
			password = this.rsaService.decrypt(password).strip();
			success = this.proxy.matches(password, encodedPassword);
		}
		return success;
	}
	
}
