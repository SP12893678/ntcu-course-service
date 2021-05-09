package com.example.demo.database;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.demo.User;
import com.example.demo.auth.JwtTokenProvider;

import io.jsonwebtoken.io.Encoders;

public class UserDB {

	public static String login(User user,JwtTokenProvider jwtTokenProvider,JdbcTemplate jdbcTemplate) {
		/*將密碼加密*/
		Encoders.BASE64.encode(user.getPassword());
		/*比對資料*/
		
		/*新增login token*/
		
	}
	
	public static void register(User user,JwtTokenProvider jwtTokenProvider,JdbcTemplate jdbcTemplate) {
		/*將密碼加密*/
		
		/*將用戶資料新增到資料表*/
		
		/*新增verify token*/
		
		/*發送信箱驗證信件*/
	}
}
