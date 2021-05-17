package com.example.demo.auth;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.User;

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
	
	public String createToken(User user) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Date date = new Date();
        Date validity = new Date(date.getTime() + (1000 * 60 * 60 * 24) * 3);
		Claims claims = Jwts.claims().setSubject(user.getName());
		claims.put("id", user.getId());
		claims.put("role", user.getRole());
		String jws = Jwts.builder()
					.setClaims(claims)
					.setExpiration(validity)
					.signWith(key)
					.compact();
		return jws;
	}
	
	public Jws<Claims> getClaim(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}
	
	/*驗證token是否過期*/
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
