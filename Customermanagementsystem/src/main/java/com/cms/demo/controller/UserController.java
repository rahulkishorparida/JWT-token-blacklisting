package com.cms.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/user")
public class UserController {
	
	@GetMapping("/profile")
	public String userprofile() {
		return "User Profile Accessed";
	}

}
