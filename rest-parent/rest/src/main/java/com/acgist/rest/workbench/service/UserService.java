package com.acgist.rest.workbench.service;

import java.util.List;

import com.acgist.rest.workbench.model.vo.UserStatisticsVo;
import com.acgist.rest.workbench.model.vo.UserVo;
import com.acgist.rest.workbench.model.vo.WorkbenchVo;

public interface UserService {

	WorkbenchVo workbench();
	
	List<UserVo> users();
	
	UserStatisticsVo userStatistics();
	
}
