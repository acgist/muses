package com.acgist.notify.sms.model.dto;

import com.acgist.boot.model.BootDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 短信通知
 * 
 * @author acgist
 */
@Getter
@Setter
public class SmsDto extends BootDto {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 手机号码
	 */
	private	String mobile;
	/**
	 * 短信内容
	 */
	private String content;
	
}
