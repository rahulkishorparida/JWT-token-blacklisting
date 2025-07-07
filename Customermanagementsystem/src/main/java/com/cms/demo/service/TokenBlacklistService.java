package com.cms.demo.service;

public interface TokenBlacklistService {
	
	 
	 public void blacklistToken(String token);
	  
	 public boolean isBlacklisted(String token);

}
