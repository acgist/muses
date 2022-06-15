package com.acgist.rest.workbench.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 工作台VO
 * 
 * @author acgist
 */
@Getter
@Setter
public class WorkbenchVo {

	/**
	 * 用户
	 */
	private List<UserVo> users;
	
	/**
	 * 用户统计
	 */
	private UserStatisticsVo userStatistics;
	
}
