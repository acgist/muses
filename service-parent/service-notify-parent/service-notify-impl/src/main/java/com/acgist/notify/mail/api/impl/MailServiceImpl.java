package com.acgist.notify.mail.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.notify.executor.NotifyService;
import com.acgist.notify.mail.api.IMailService;
import com.acgist.notify.mail.model.dto.MailDto;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class MailServiceImpl implements IMailService {

	@Autowired
	private NotifyService notifyService;
	
	@Override
	public boolean notify(MailDto mailDto) {
		return this.notifyService.notify(mailDto);
	}

}
