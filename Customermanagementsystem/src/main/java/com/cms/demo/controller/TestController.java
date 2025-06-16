package com.cms.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.cms.demo.dto.UserResponse;
import com.cms.demo.service.TokenBlacklistService;
import com.cms.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth") 
public class TestController {
	
	@Autowired
	private UserService userService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @GetMapping("/hello")
    public String hello() {
        return "Authenticated Hello!!!!!!!!!!!!!!!!!!!!!!!";
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserResponse response){
    	String token = userService.login(response);
    	if(token == null) {
    		return new ResponseEntity<>("invalid",HttpStatus.BAD_REQUEST);
    	}
		return new ResponseEntity<>(token, HttpStatus.OK);
    	
    }
    
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//        return ResponseEntity.ok("You have been logged out successfully.");
//    }
    
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            tokenBlacklist.blacklistToken(token);
//            return ResponseEntity.ok("You have been logged out. Token blacklisted.");
//        }
//        return ResponseEntity.badRequest().body("Token not found.");
//    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
//            System.out.println("Logging out token: " + token);
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("Token blacklisted and user logged out.");
        }

        return ResponseEntity.badRequest().body("No token found.");
    }
 
}
