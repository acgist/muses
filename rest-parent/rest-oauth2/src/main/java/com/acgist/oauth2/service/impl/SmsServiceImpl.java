package com.acgist.oauth2.service.impl;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.acgist.oauth2.service.SmsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsServiceImpl implements SmsService {

	/**
	 * 短信验证码
	 */
	private Map<String, String> smsCode = new ConcurrentHashMap<>();
	
	/**
	 * 发送短信验证码
	 * 
	 * @param mobile 手机号码
	 */
	@Override
	public void send(String mobile) {
		// TODO：自行实现发送
		final Random random = new Random();
		final StringBuilder builder = new StringBuilder();
		for (int index = 0; index < SmsService.SMS_CODE_LENGTH; index++) {
			builder.append(random.nextInt(10));
		}
		this.smsCode.put(mobile, builder.toString());
		log.info("短信验证码：{}-{}", mobile, builder.toString());
	}
	
	/**
	 * 验证短信验证码
	 * 
	 * @param mobile 手机号码
	 * @param smsCode 短信验证码
	 * 
	 * @return 是否成功
	 */
	@Override
	public boolean verify(String mobile, String smsCode) {
		return StringUtils.equals(this.smsCode.get(mobile), smsCode);
	}
	
}
