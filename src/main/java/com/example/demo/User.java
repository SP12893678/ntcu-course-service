package com.example.demo;

import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.User.Role;
import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.database.RoleDB;
import com.example.demo.database.UserDB;

public class User {

	private int id;
	private String name;
	private String email;
	private String password;
	private String loginToken;
	private String verifyToken;
	private Role role;
	private boolean isVerify;
	private String salt;
	
	public String login(JwtTokenProvider jwtTokenProvider,JdbcTemplate jdbcTemplate) {
//		try {
			List<Map<String, Object>> userList = UserDB.serachUserByEmail(getEmail(),jdbcTemplate);
			if(userList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with Email.");
			setSalt((String) userList.get(0).get("Salt"));
			/*將密碼加密*/
//	        setPassword(encryptPassword(jwtTokenProvider));
	        
	        /*登入驗證*/
	        boolean isValid = checkPassword((String) userList.get(0).get("Password")); 
			if(!isValid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with Password.");
			
			/*確認是否已信箱認證*/
			if(!(boolean) userList.get(0).get("IsVerify")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Need to verify email.");
			
			setId((int) userList.get(0).get("ID"));
			setEmail((String) userList.get(0).get("Email"));
			setIsVerify((boolean) userList.get(0).get("IsVerify"));
			
			/*取得使用者資料(名稱、身分)*/
			List<Map<String, Object>> userRoleList = UserDB.getRoleIDByID(getId(), jdbcTemplate);
			if(userRoleList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"user's role is empty");
			
			List<Map<String, Object>> roleList = RoleDB.getRoleByID((int) userRoleList.get(0).get("RoleID"), jdbcTemplate);
			if(roleList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with user's role.");
			
			setRole(new Role((Map<String, Object>) roleList.get(0)));
			setLoginToken(jwtTokenProvider.createToken(this));
			
			/*新增login token*/
			int result = UserDB.updateLoginToken(getId(),getLoginToken(),jdbcTemplate);
			if(result>0) return getLoginToken();
			else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with create the loignToken.");
	        
//		} catch (Exception e) {
//			System.out.println(e);
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with login.");
//		}
	}
	
	public void register(JavaMailSender mailSender,JwtTokenProvider jwtTokenProvider,JdbcTemplate jdbcTemplate) {
		try {
			/*將密碼加密*/
			setSalt(BCrypt.gensalt(11));
	        setPassword(encryptPassword(jwtTokenProvider));
	          
			/*將用戶資料新增到資料表*/
	        int result = UserDB.insertUser(this,jdbcTemplate);
	        if(result<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with insert user data.");
	        
	        /*取得使用者資料*/
	        List<Map<String, Object>> userList = UserDB.serachUserByLogin(this,jdbcTemplate);
			if(userList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with Email or Password.");
			setId((int) userList.get(0).get("ID"));

			/*新增verify token*/
			setVerifyToken(jwtTokenProvider.createToken(this));
			result = UserDB.updateVerifyToken(getId(),getVerifyToken(),jdbcTemplate);
			
			/*發送信箱驗證信件*/
			if(result>0) sendVerifyMail(mailSender);
			else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with update verify token.");
	        
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with register.");
		}
	}
	
	public void sendVerifyMail(JavaMailSender mailSender) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("office1289367@gmail.com");
        mail.setTo("office1289366@gmail.com");
        mail.setSubject("會員註冊信箱驗證");
        mail.setText("<a href=\"http:127.0.0.1:9090/api/auth/verify/"+ getVerifyToken() + "\">"+ getVerifyToken() +"</a>");
        mailSender.send(mail);
	}
	
	public String encryptPassword(JwtTokenProvider jwtTokenProvider)  {
		return BCrypt.hashpw(getPassword(), getSalt());
	}
	
	public boolean checkPassword(String hashed) {
		return BCrypt.checkpw(getPassword(), hashed);
	}
	
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	
	public String getLoginToken() {
		return loginToken;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getIsVerify() {
		return isVerify;
	}

	public void setIsVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}

	public String getVerifyToken() {
		return verifyToken;
	}

	public void setVerifyToken(String verifyToken) {
		this.verifyToken = verifyToken;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public class Role{
		private int id;
		private String name;
		private int layer;
		
		public Role(Map<String, Object> object) {
			this.id = (int) object.get("id");
			this.name = (String) object.get("name");
			this.layer = (int) object.get("layer");
		}

		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public int getLayer() {
			return layer;
		}
		
		public void setLayer(int layer) {
			this.layer = layer;
		}
		
	}
}
