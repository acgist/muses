package com.acgist.notify.mail.model.dto;

import com.acgist.boot.model.BootDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 发送邮件
 * 
 * @author acgist
 */
@Getter
@Setter
public class MailDto extends BootDto {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 收件用户
	 */
	private String mail;
	/**
	 * 主题
	 */
	private String subject;
	/**
	 * 内容
	 */
	private String content;
	
}
