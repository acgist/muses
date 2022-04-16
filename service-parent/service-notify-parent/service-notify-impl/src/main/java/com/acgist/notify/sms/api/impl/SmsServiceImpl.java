package com.acgist.notify.sms.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.notify.executor.NotifyService;
import com.acgist.notify.sms.api.ISmsService;
import com.acgist.notify.sms.model.dto.SmsDto;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class SmsServiceImpl implements ISmsService {
	
	@Autowired
	private NotifyService notifyService;

	@Override
	public boolean notify(SmsDto smsDto) {
		return this.notifyService.notify(smsDto);
	}

}
