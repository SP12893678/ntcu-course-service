package com.example.demo;

import java.util.Map;

public class Course {

	private String txtYears;
	private String txtTerm;
	private String ddlEdu;
	private String ddlDept;
	private String code;
	
	public Course() {
		
	}
	
	public Course(Map<String,Object> map) {
		setTxtYears((String) map.get("txtYears"));
		setTxtTerm((String) map.get("txtTerm"));
		setDdlEdu((String) map.get("ddlEdu"));
		setDdlDept((String) map.get("ddlDept"));
		setCode((String) map.get("code"));
	}
	
	public void setTxtYears(String txtYears) {
		this.txtYears = txtYears;
	}
	
	public void setTxtTerm(String txtTerm) {
		this.txtTerm = txtTerm;
	}
	
	public void setDdlEdu(String ddlEdu) {
		this.ddlEdu = ddlEdu;
	}
	
	public void setDdlDept(String ddlDept) {
		this.ddlDept = ddlDept;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getTxtYears() {
		return this.txtYears;
	}
	
	public String getTxtTerm() {
		return this.txtTerm;
	}
	
	public String getDdlEdu() {
		return this.ddlEdu;
	}
	
	public String getDdlDept() {
		return this.ddlDept;
	}
	
	public String getCode() {
		return this.code;
	}
	
	@Override
	public String toString() {
		return "{txtYears:" + this.txtYears + 
				", txtTerm:" + this.txtTerm + 
				", ddlEdu:" + this.ddlEdu + 
				", ddlDept:" + this.ddlDept + 
				", code:" + this.code + "}";
	}
}
