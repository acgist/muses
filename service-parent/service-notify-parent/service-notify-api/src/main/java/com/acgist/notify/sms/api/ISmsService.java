package com.acgist.notify.sms.api;

import com.acgist.notify.sms.model.dto.SmsDto;

/**
 * 发送短信通知
 * 
 * @author acgist
 */
public interface ISmsService {

	/**
	 * 发送短信
	 * 
	 * @param smsDto 短信信息
	 * 
	 * @return 是否发送成功
	 */
	boolean notify(SmsDto smsDto);
	
}
