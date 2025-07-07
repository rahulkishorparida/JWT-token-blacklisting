package com.cms.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cms.demo.model.TokenBlacklist;
import com.cms.demo.service.JwtService;
import com.cms.demo.service.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtFilter extends OncePerRequestFilter {
	//Ensures the filter is executed once per HTTP request
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private TokenBlacklistService tokenBlacklistService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
	
		if(authHeader!= null&& authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtService.extractUserName(token);
			
		     //  Check blacklist only after extracting token
	        if (tokenBlacklistService.isBlacklisted(token)) {
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted.");
	            return;
	        }
		}
		
		if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null) {
			//Ensures that no one is currently authenticated in the security context.
			//Prevents overwriting authentication if already set.
		
			UserDetails userDetails= userDetailsServiceImpl.loadUserByUsername(username);
           //fetch user data from DB like password & role
			
			Boolean validateToken =jwtService.validateToken(token, userDetails);
			//The token's username matches the DB user.
			//The token is not expired.
			if(validateToken) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				//Spring’s object representing a successful authentication.
				//Tells Spring Security: "this user is authenticated and has these roles."
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//Attach extra information about the current request (like IP address) to the user's authentication."				
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
