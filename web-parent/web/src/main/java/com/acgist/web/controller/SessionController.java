package com.acgist.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

	@GetMapping("/session")
	public String session(String value, HttpServletRequest request) {
		value = value == null ? "session" : value;
		request.getSession().setAttribute("session", value);
		return value;
	}

	@GetMapping("/session/value")
	public String sessionValue(String value, HttpServletRequest request) {
		return (String) request.getSession().getAttribute("session");
	}

}
