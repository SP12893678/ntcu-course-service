package com.example.demo.database;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.demo.User;
import com.example.demo.auth.JwtTokenProvider;

import io.jsonwebtoken.io.Encoders;

public class UserDB {

	public static String login(User user,JwtTokenProvider jwtTokenProvider,JdbcTemplate jdbcTemplate) {
		/*�N�K�X�[�K*/
		Encoders.BASE64.encode(user.getPassword());
		/*�����*/
		
		/*�s�Wlogin token*/
		
	}
	
	public static void register(User user,JwtTokenProvider jwtTokenProvider,JdbcTemplate jdbcTemplate) {
		/*�N�K�X�[�K*/
		
		/*�N�Τ��Ʒs�W���ƪ�*/
		
		/*�s�Wverify token*/
		
		/*�o�e�H�c���ҫH��*/
	}
}
