package com.acgist.rest.workbench.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acgist.rest.workbench.model.vo.UserStatisticsVo;
import com.acgist.rest.workbench.model.vo.UserVo;
import com.acgist.rest.workbench.model.vo.WorkbenchVo;
import com.acgist.rest.workbench.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public WorkbenchVo workbench() {
		final WorkbenchVo workbench = new WorkbenchVo();
		workbench.setUsers(this.users());
		workbench.setUserStatistics(this.userStatistics());
		return workbench;
	}

	@Override
	public List<UserVo> users() {
		return null;
	}

	@Override
	public UserStatisticsVo userStatistics() {
		return null;
	}

}
