package com.cms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.demo.model.TokenBlacklist;

import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByToken(String token);
}

