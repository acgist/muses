package com.acgist.web.controller.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.web.UserContext;
import com.acgist.web.config.CurrentUser;

@Controller
@RequestMapping("/user")
public class UserController {

	@GetMapping
	public String index() {
		return "/user/index";
	}

	@GetMapping("/name")
	@ResponseBody
	public String name() {
		return UserContext.currentUser().getUsername();
	}
	
	@GetMapping("/current")
	@ResponseBody
	public String current(@CurrentUser User user) {
		return user.getUsername();
	}

}
