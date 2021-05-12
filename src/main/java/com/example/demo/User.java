package com.example.demo;

public class User {

	private int id;
	private String name;
	private String email;
	private String password;
	private String loginToken;
	private String verifyToken;
	private Role role;
	private int isVerify;
	
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

	public int getIsVerify() {
		return isVerify;
	}

	public void setIsVerify(int isVerify) {
		this.isVerify = isVerify;
	}

	public String getVerifyToken() {
		return verifyToken;
	}

	public void setVerifyToken(String verifyToken) {
		this.verifyToken = verifyToken;
	}

	public class Role{
		private int id;
		private String name;
		private int layer;
		
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
