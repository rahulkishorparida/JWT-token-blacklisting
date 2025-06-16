package com.cms.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TokenBlacklist {
    
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(length = 500, unique = true)
	    private String token;

	    private LocalDateTime blacklistedAt;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public LocalDateTime getBlacklistedAt() {
			return blacklistedAt;
		}

		public void setBlacklistedAt(LocalDateTime blacklistedAt) {
			this.blacklistedAt = blacklistedAt;
		}

}
