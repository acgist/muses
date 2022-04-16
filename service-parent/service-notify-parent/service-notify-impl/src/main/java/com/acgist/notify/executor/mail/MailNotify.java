package com.acgist.notify.executor.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.acgist.notify.executor.Notify;
import com.acgist.notify.mail.model.dto.MailDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Mail通知
 * 
 * 如果需要自定义注入参考：MailSenderPropertiesConfiguration
 * 
 * @author acgist
 */
@Slf4j
public class MailNotify extends Notify<MailDto, MailNotifyConfig> {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public MailNotify(MailNotifyConfig config) {
		super(MailDto.class, config);
	}
	
	@Override
	public boolean execute(MailDto mailDto) {
		log.debug("发送Mail通知：{}", mailDto);
		final SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(mailDto.getMail());
		message.setFrom(this.notifyConfig.getFrom());
		message.setSubject(mailDto.getSubject());
		message.setText(mailDto.getContent());
		this.javaMailSender.send(message);
		return true;
	}

}
