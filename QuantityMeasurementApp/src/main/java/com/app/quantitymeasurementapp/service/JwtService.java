package com.app.quantitymeasurementapp.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
	private static final String SECRET = "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5MTI=";
	 private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
	private Key signingKey;
	
	@PostConstruct
	public void init() {
	    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
	}
	public String genrateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractEmail(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(signingKey).build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public boolean validateToken(String token, String email) {
		return extractEmail(token).equals(email) && !isTokenExpired(token);
	}
	
	public boolean isTokenExpired(String token) {
		return Jwts.parserBuilder().setSigningKey(signingKey).build()
				.parseClaimsJws(token).getBody().getExpiration().before(new Date());
	}
}
