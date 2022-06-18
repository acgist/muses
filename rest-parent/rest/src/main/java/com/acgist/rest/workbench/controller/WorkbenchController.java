package com.acgist.rest.workbench.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.boot.model.Message;
import com.acgist.rest.workbench.model.vo.UserStatisticsVo;
import com.acgist.rest.workbench.model.vo.UserVo;
import com.acgist.rest.workbench.model.vo.WorkbenchVo;
import com.acgist.rest.workbench.service.UserService;

/**
 * 工作台
 * 
 * @author acgist
 */
@RestController
@RequestMapping("/workbench")
public class WorkbenchController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public Message<WorkbenchVo> workbench() {
		return Message.success(this.userService.workbench());
	}
	
	@GetMapping("/users")
	public Message<List<UserVo>> users() {
		return Message.success(this.userService.users());
	}
	
	@GetMapping("/user/statistics")
	public Message<UserStatisticsVo> userStatistics() {
		return Message.success(this.userService.userStatistics());
	}
	
}
