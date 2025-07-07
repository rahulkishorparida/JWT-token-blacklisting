package com.cms.demo.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.cms.demo.dto.UserResponse;
import com.cms.demo.model.User;
import com.cms.demo.repository.UserRepository;
import com.cms.demo.service.JwtService;
import com.cms.demo.service.UserService;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
	private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
	
    @Override
    public String login(UserResponse response) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(response.getUsername(), response.getPassword())
        );

        if (authentication.isAuthenticated()) {
            User user = userRepository.findByUsername(response.getUsername());
//                    .orElseThrow(() -> new RuntimeException("User not found"));

            return jwtService.generateToken(user)
                    .orElseThrow(() -> new RuntimeException("Token generation failed"));
        }
        
        throw new RuntimeException("Authentication failed for user: " + response.getUsername());
    }

	


}
