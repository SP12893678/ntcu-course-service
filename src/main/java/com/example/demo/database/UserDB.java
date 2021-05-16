package com.example.demo.database;


import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;
import com.example.demo.User;


public class UserDB {
	
	public static List<Map<String, Object>> serachUserByID(int id, JdbcTemplate jdbcTemplate){
		String condition = "ID = '" + id + "'";
		return jdbcTemplate.queryForList("SELECT * FROM member where " + condition);
	}
	
	public static List<Map<String, Object>> serachUserByEmail(String email, JdbcTemplate jdbcTemplate){
		String condition = "Email = '" + email + "'";
		return jdbcTemplate.queryForList("SELECT * FROM member where " + condition);
	}
	
	public static List<Map<String, Object>> serachUserByLogin(User user, JdbcTemplate jdbcTemplate){
		String condition = "Email = '" + user.getEmail() + 
							"' and Password = '" + user.getPassword() + "'";
		return jdbcTemplate.queryForList("SELECT * FROM member where " + condition);
	}
	
	public static int updateLoginToken(int userID, String loginToken, JdbcTemplate jdbcTemplate) {
		String condition = "ID = '" + userID + "'";
		String set = "LoginToken = '" + loginToken + "'";
		int status = jdbcTemplate.update("UPDATE member Set " + set + " Where " + condition);
		return status;
	}
	
	public static int updateVerifyToken(int userID, String verifyToken, JdbcTemplate jdbcTemplate) {
		String condition = "ID = '" + userID + "'";
		String set = "VerifyToken = '" + verifyToken + "'";
		int status = jdbcTemplate.update("UPDATE member Set " + set + " Where " + condition);
		return status;
	}
	
	public static int updateVerifyTag(int userID, String verifyToken, JdbcTemplate jdbcTemplate) {
		String condition = "ID = " + userID + 
							" and verifyToken = '" + verifyToken + "'";
		String set = "IsVerify = '1'";
		int status = jdbcTemplate.update("UPDATE member Set " + set + " Where " + condition);
		return status;
	}
	
	public static int insertUser(User user, JdbcTemplate jdbcTemplate) {
		String key = "(Name,Email,Password,Salt)";
		String value =  "('"+ String.join("','", 
						user.getName(),
						user.getEmail(),
						user.getPassword(),
						user.getSalt())+
						"')";
		int status = jdbcTemplate.update("Insert Into member " + key + "Values" + value);
		return status;
	}
	
	public static List<Map<String, Object>> getRoleIDByID(int userID, JdbcTemplate jdbcTemplate) {
		String condition = "UserID = " + userID;
		return jdbcTemplate.queryForList("SELECT * FROM `member-role` where " + condition);
	}
}
