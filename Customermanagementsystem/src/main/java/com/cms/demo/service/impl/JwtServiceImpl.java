package com.cms.demo.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cms.demo.model.User;
import com.cms.demo.repository.UserRepository;
import com.cms.demo.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public Optional<String> generateToken(User user) {	
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());

        String token = Jwts.builder()
        		.setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getKey())
                .compact();

        return Optional.of(token);
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
     // creates a secure SecretKey object suitable for HMAC-SHA algorithms 
    // creates a secret key from a byte array for signing a JWT using HMAC-SHA algorithms 
    }

    @Override
    public Claims extractAllClaims(String token) {
        Claims claims= Jwts.parserBuilder()
                .setSigningKey(getKey()) // getKey() decodes and returns the correct Key
                .build()
                //Builds the parser with all the configurations you set (like the key).
                //After .build(), you now have a parser object ready to use
                .parseClaimsJws(token)
                //Parses the JWS (JSON Web Signature), i.e., your JWT token.
                // Validates:
                 	//The token structure.
                 	//The signature (using the signing key).
                 	//The expiration date (if present).
                .getBody();
        return claims;
    }
 //parses and validates the JWT token using the secret key and then returns the claims (data inside the token)   
    	
	
	@Override
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
   //Function<Claims, T> claimResolver: a functional interface (Java 8+) 
   //— a function that takes Claims as input and returns a T value.		
		Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	@Override
	public String extractUserName(String token) {
	return	extractClaim(token, Claims::getSubject);
		
	}
	@Override
	public Date extractExpairation(String token) {
		return extractClaim(token,Claims::getExpiration);
		//method reference
		//token validation
	}
	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
	String userName = extractUserName(token);
	Boolean isexpired=  isTokenExpaired(token);
	if(userName.equals(userDetails.getUsername()) && !isexpired) {
		return true;
	}
	return false;	
	}

	private Boolean isTokenExpaired(String token) {
		Date expairationDate = extractExpairation(token);
		return expairationDate.before(new Date());
	}

}
