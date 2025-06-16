//package com.cms.demo.config;
//
//import com.cms.demo.model.BlacklistedToken;
//import com.cms.demo.repository.BlacklistedTokenRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TokenBlacklist {
//
//    @Autowired
//    private BlacklistedTokenRepository tokenRepository;
//
//    public void blacklistToken(String token) {
//        if (!isBlacklisted(token)) {
//            BlacklistedToken blacklistedToken = new BlacklistedToken();
//            blacklistedToken.setToken(token);
//            tokenRepository.save(blacklistedToken);
//        }
//    }
//
//    public boolean isBlacklisted(String token) {
//        return tokenRepository.findByToken(token).isPresent();
//    }
//}
