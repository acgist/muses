package com.acgist.rest.workbench.service;

import java.util.List;

import com.acgist.rest.workbench.model.vo.UserStatisticsVo;
import com.acgist.rest.workbench.model.vo.UserVo;
import com.acgist.rest.workbench.model.vo.WorkbenchVo;

/**
 * 用户
 * 
 * @author acgist
 */
public interface UserService {

	/**
	 * @return 工作台
	 */
	WorkbenchVo workbench();
	
	/**
	 * @return 用户列表
	 */
	List<UserVo> users();
	
	/**
	 * @return 用户统计
	 */
	UserStatisticsVo userStatistics();
	
}
