package com.example.demo;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.database.CourseDB;
import com.example.demo.database.NoticeDB;

@Component
public class CourseDetector {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public CourseDetector() {
		
	}
	
	public void run() {
		System.out.print(new JwtTokenProvider().getKey());
		
		
		/*���o���ݬd�ߪ��ҵ{�q���M��*/
		List<Map<String, Object>> noticeList = NoticeDB.searchAllWaitingCourseNotice(jdbcTemplate);
		System.out.print(noticeList.get(0).toString());
		/*���o�Q�d�ߪ��ҵ{*/
		List<Map<String, Object>> courseList = CourseDB.getCourseByID((int) noticeList.get(0).get("Course_ID"), jdbcTemplate);
		System.out.print(courseList.get(0).toString());
		
		/*�d�߽ҵ{�H�ƾl�B*/
		int nums = CourseHelper.getCourseRemainingNums(new Course(courseList.get(0)));
		System.out.println(nums);
	}
}
