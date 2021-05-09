package com.example.demo;

public class HTTPRequestBody {
	private User user;
	private Course course;
	private String loginToken;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void Course(Course course) {
		this.course = course;
	}
	
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public Course getCourse() {
		return this.course;
	}
	
	public String getLoginToken() {
		return this.loginToken;
	}
}
