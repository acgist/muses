package com.acgist.oauth2.service;

/**
 * 短信服务
 * 
 * @author acgist
 */
public interface SmsService {

	/**
	 * 短信验证码长度
	 */
	int SMS_CODE_LENGTH = 6;
	
	/**
	 * 发送短信验证码
	 * 
	 * @param mobile 手机号码
	 */
	void send(String mobile);
	
	/**
	 * 验证短信验证码
	 * 
	 * @param mobile 手机号码
	 * @param smsCode 短信验证码
	 * 
	 * @return 是否成功
	 */
	boolean verify(String mobile, String smsCode);
	
}
