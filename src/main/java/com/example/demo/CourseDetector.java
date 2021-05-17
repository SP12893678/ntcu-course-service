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
		
		
		/*取得等待查詢的課程通知清單*/
		List<Map<String, Object>> noticeList = NoticeDB.searchAllWaitingCourseNotice(jdbcTemplate);
		System.out.print(noticeList.get(0).toString());
		/*取得被查詢的課程*/
		List<Map<String, Object>> courseList = CourseDB.getCourseByID((int) noticeList.get(0).get("Course_ID"), jdbcTemplate);
		System.out.print(courseList.get(0).toString());
		
		/*查詢課程人數餘額*/
		int nums = CourseHelper.getCourseRemainingNums(new Course(courseList.get(0)));
		System.out.println(nums);
	}
}
