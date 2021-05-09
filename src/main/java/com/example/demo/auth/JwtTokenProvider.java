package com.example.demo.auth;

import java.security.Key;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Base64.*;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${access_token_secret}")
	private String secretKey;

	
	public String getKey() {
		return secretKey;
	}
	
	public String createToken() {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		Date validity = new Date();
		LocalDateTime.from(validity.toInstant()).plusDays(3);
		String jws = Jwts.builder()
					.setSubject("Joe")
					.setExpiration(validity)
					.signWith(key)
					.compact();
		
		System.out.print(jws);
		return jws;
	}
	
	/*����token�O�_�L��*/
	public boolean validateToken(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		}catch(JwtException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Expired or invalid JWT token");
		}
	}
	
//	private String generateSafeToken() {
//	    SecureRandom random = new SecureRandom();
//	    byte bytes[] = new byte[64];
//	    random.nextBytes(bytes);
//	    Encoder encoder = Base64.getUrlEncoder().withoutPadding();
//	    String token = encoder.encodeToString(bytes);
//	    return token;
//	}
}