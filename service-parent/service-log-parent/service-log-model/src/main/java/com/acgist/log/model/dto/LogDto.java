package com.acgist.log.model.dto;

import java.util.Map;

import com.acgist.log.model.es.Log;

import lombok.Getter;
import lombok.Setter;

/**
 * 日志变化记录
 * 
 * @author acgist
 */
@Getter
@Setter
public class LogDto extends Log {

	private static final long serialVersionUID = 1L;

	/**
	 * 原始数据对象
	 */
	private Object sourceObject;
	/**
	 * 差异数据对象
	 */
	private Object diffObject;
	/**
	 * 差异数据
	 */
	private Map<String, Object> diffMap;
	/**
	 * 日志模板输出信息
	 */
	private String log;

}
