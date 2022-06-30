package com.acgist.www.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.boot.service.CacheService;
import com.acgist.boot.service.RsaService;
import com.acgist.www.utils.WebUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * RSA加密
 * 
 * @author acgist
 */
public class RsaPasswordEncoder implements PasswordEncoder {

	/**
	 * 缓存KEY
	 */
	private static final String CACHE = "password::fail";
	/**
	 * 代理
	 */
	private BCryptPasswordEncoder proxy = new BCryptPasswordEncoder();
	
	@Value("${system.password.fail.time:10}")
	private long failTime;
	@Value("${system.password.lock.time:600000}")
	private long lockTime;
	
	@Autowired
	private RsaService rsaService;
	@Autowired
	private CacheService cacheService;
	
	@Override
	public String encode(CharSequence rawPassword) {
		return this.proxy.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		final PasswordFail passwordFail = this.checkPasswordFail();
		// 校验密码
		String password = rawPassword.toString();
		boolean success = this.proxy.matches(password, encodedPassword);
		if(!success && password.length() > 64) {
			password = this.rsaService.decrypt(password);
			success = this.proxy.matches(password, encodedPassword);
		}
		this.passwordFail(success, passwordFail);
		return success;
	}
	
	/**
	 * 校验登陆
	 */
	private PasswordFail checkPasswordFail() {
		PasswordFail passwordFail = null;
		// 验证失败次数
		final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		// TODO：JDK17
		if(requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			final String clientIP = WebUtils.clientIP(((ServletRequestAttributes) requestAttributes).getRequest());
			passwordFail = this.cacheService.cache(CACHE, clientIP);
			if(passwordFail == null) {
				passwordFail = new PasswordFail(clientIP);
				passwordFail.reset();
			} else {
				if(passwordFail.failTime > this.failTime) {
					// 过期删除缓存
					if(System.currentTimeMillis() - passwordFail.lockTime > this.lockTime) {
						passwordFail.reset();
						this.cacheService.remove(CACHE, clientIP);
					} else {
						throw MessageCodeException.of("帐号已被锁定");
					}
				}
			}
		}
		return passwordFail;
	}

	/**
	 * 设置登陆
	 */
	private void passwordFail(boolean success, PasswordFail passwordFail) {
		// 记录失败次数
		if(passwordFail != null) {
			if(success) {
				passwordFail.reset();
				this.cacheService.remove(CACHE, passwordFail.clientIP);
			} else {
				passwordFail.fail();
				this.cacheService.cache(CACHE, passwordFail.clientIP, passwordFail);
			}
		}
	}
	
	/**
	 * 登陆失败次数
	 * 
	 * @author acgist
	 */
	@Getter
	@Setter
	public static class PasswordFail {
		
		/**
		 * 失败次数
		 */
		private Long failTime;
		/**
		 * 锁定时间
		 */
		private Long lockTime;
		/**
		 * 客户端的IP
		 */
		private String clientIP;
		
		public PasswordFail() {
		}
		
		public PasswordFail(String clientIP) {
			this.clientIP = clientIP;
		}
		
		/**
		 * 添加失败
		 */
		private void fail() {
			this.failTime = this.failTime + 1;
			this.lockTime = System.currentTimeMillis();
		}
		
		/**
		 * 重置
		 */
		private void reset() {
			this.failTime = 0L;
			this.lockTime = null;
		}
		
	}

}
