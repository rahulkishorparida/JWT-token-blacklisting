package com.cms.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.cms.demo.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public interface JwtService {


	public Optional<String> generateToken(User user);

	public Claims extractAllClaims(String token);
	
	public <T> T extractClaim(String token, Function<Claims, T> claimResolve);

	public String extractUserName(String token);
	
	public Date extractExpairation(String  token);

	public Boolean validateToken(String token, UserDetails userDetails);
		
		
	

}
