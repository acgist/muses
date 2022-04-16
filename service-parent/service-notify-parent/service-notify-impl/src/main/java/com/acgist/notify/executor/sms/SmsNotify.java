package com.acgist.notify.executor.sms;

import com.acgist.notify.executor.Notify;
import com.acgist.notify.sms.model.dto.SmsDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Sms通知
 * 
 * @author acgist
 */
@Slf4j
public class SmsNotify extends Notify<SmsDto, SmsNotifyConfig> {

	public SmsNotify(SmsNotifyConfig config) {
		super(SmsDto.class, config);
	}
	
	@Override
	public boolean execute(SmsDto smsDto) {
		log.debug("发送Sms通知：{}", smsDto);
		return true;
	}

}
