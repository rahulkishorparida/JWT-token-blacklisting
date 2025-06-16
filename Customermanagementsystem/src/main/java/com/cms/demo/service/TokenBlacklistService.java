package com.cms.demo.service;

public interface TokenBlacklistService {
	
	 public boolean isBlacklisted(String token);
	 
	 public void blacklistToken(String token);
	  

}
