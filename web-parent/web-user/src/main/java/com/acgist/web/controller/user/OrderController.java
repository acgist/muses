package com.acgist.web.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/order")
public class OrderController {

	@GetMapping
	public String index(String orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "/user/order/index";
	}

	@PostMapping
	public String index(String orderId) {
		return "redirect:/user/order?orderId=" + orderId;
	}
	
}
