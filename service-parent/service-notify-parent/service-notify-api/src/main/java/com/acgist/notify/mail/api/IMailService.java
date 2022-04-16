package com.acgist.notify.mail.api;

import com.acgist.notify.mail.model.dto.MailDto;

/**
 * 邮件通知
 * 
 * @author acgist
 */
public interface IMailService {

	/**
	 * 发送邮件
	 * 
	 * @param mailDto 邮件信息
	 * 
	 * @return 是否发送成功
	 */
	boolean notify(MailDto mailDto);
	
}
