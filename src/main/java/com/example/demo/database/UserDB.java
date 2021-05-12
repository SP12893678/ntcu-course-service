package com.example.demo.database;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Course;
import com.example.demo.User;
import com.example.demo.User.Role;
import com.example.demo.auth.JwtTokenProvider;



public class UserDB {
	
	public static String login(User user,JwtTokenProvider jwtTokenProvider,JdbcTemplate jdbcTemplate) {
		try {
			/*將密碼加密*/
	        String encryptedPassword = encryptPassword(user,jwtTokenProvider);
	        user.setPassword(encryptedPassword);
	        
	        /*比對資料庫驗證登入*/
			String condition = "Email = '" + user.getEmail() + 
								"' and Password = '" + user.getPassword() + "'";
			List<Map<String, Object>> userList = jdbcTemplate.queryForList("SELECT * FROM user where " + condition);
			if(userList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with Email or Password.");
			if((int) userList.get(0).get("IsVerify")==0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Need to verify email.");
			
			user.setId((int) userList.get(0).get("ID"));
			user.setEmail((String) userList.get(0).get("Email"));
			user.setIsVerify((int) userList.get(0).get("IsVerify"));
			
			
			/*取得使用者資料(名稱、身分)*/
			List<Map<String, Object>> userRoleList = getRoleIDByID(user.getId(), jdbcTemplate);
			if(userRoleList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"user's role is empty");
			
			List<Map<String, Object>> roleList = RoleDB.getRoleByID((int) userRoleList.get(0).get("RoleID"), jdbcTemplate);
			if(roleList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with user's role.");
			
			user.setRole((Role) roleList.get(0));
			
			
			/*新增login token*/
			String loginToken = jwtTokenProvider.createToken(user);
			user.setLoginToken(loginToken);
			
			int result = updateLoginToken(user.getId(),user.getLoginToken(),jdbcTemplate);
			if(result>0) return user.getLoginToken();
			else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with create the loignToken.");
	        
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with login.");
		}
	}
	
	public static void register(User user,JavaMailSender mailSender,JwtTokenProvider jwtTokenProvider,JdbcTemplate jdbcTemplate) {
		try {
			System.out.print("Send Mail");
			
			/*將密碼加密*/
	        String encryptedPassword = encryptPassword(user,jwtTokenProvider);
	        user.setPassword(encryptedPassword);
			
	        System.out.print("Send Mail0");
	        
			/*將用戶資料新增到資料表*/
	        int result = insertUser(user,jdbcTemplate);
	        if(result<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with insert user data.");
	        
	        System.out.print("Send Mail1");
	        
	        /*取得使用者資料*/
			String condition = "Email = '" + user.getEmail() + 
								"' and Password = '" + user.getPassword() + "'";
			List<Map<String, Object>> userList = jdbcTemplate.queryForList("SELECT * FROM member where " + condition);
			if(userList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with Email or Password.");
			user.setId((int) userList.get(0).get("ID"));
	        
			System.out.print("Send Mail2");
	        
			/*新增verify token*/
	        String verifyToken = jwtTokenProvider.createToken(user);
			user.setVerifyToken(verifyToken);
			
			result = updateVerifyToken(user.getId(),user.getVerifyToken(),jdbcTemplate);
			
			
			/*發送信箱驗證信件*/
			if(result>0) {
	//	        SimpleMailMessage mail = new SimpleMailMessage();
	//	        mail.setFrom("office1289367@gmail.com");
	//	        mail.setTo("office1289366@gmail.com");
	//	        mail.setSubject("主旨：會員註冊信箱驗證");
	//	        mail.setText("內容：這是一封測試信件，恭喜您成功發送了唷");
	//	        mailSender.send(mail);
				System.out.print("Send Mail");
			}
			else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with update verify token.");
	        
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with register.");
		}
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
	
	public static int insertUser(User user, JdbcTemplate jdbcTemplate) {
		String key = "(Name,Email,Password)";
		String value =  "('"+ String.join("','", 
						user.getName(),
						user.getEmail(),
						user.getPassword()) +
						"')";
		int status = jdbcTemplate.update("Insert Into member " + key + "Values" + value);
		return status;
	}
	
	public static List<Map<String, Object>> getRoleIDByID(int userID, JdbcTemplate jdbcTemplate) {
		String condition = "ID = " + userID;
		return jdbcTemplate.queryForList("SELECT * FROM member-role where " + condition);
	}
	
	public static String encryptPassword(User user, JwtTokenProvider jwtTokenProvider)  {
        try {
        	

		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
