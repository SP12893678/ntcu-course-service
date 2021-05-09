package com.example.demo;

public class User {

	private String name;
	private String email;
	private String password;
	private String loginToken;
	
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
}
