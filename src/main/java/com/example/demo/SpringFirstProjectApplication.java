package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.demo.auth.JwtTokenProvider;

@SpringBootApplication
public class SpringFirstProjectApplication {
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringFirstProjectApplication.class, args);
		CourseDetector courseDetector = context.getBean(CourseDetector.class);
//		courseDetector.run();
	}
}
