package com.example.demo.database;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;


import com.example.demo.Course;

public class CourseDB {

//	@Autowired
//	private static JdbcTemplate jdbcTemplate;
	
	private CourseDB() {
		
	}
	
	/*
	 * 取得課程藉由課程ID
	 */
	public static List<Map<String, Object>> getCourseByID(int courseID, JdbcTemplate jdbcTemplate) {
		String condition = "id = " + courseID;
		return jdbcTemplate.queryForList("SELECT * FROM course where " + condition);
	}
	
	/*
	 * 查詢課程是否存在於課程資料表
	 */
	public static List<Map<String, Object>> searchCourse(Course course, JdbcTemplate jdbcTemplate) {
		String condition = "txtYears = '" + course.getTxtYears() + 
				"' and txtTerm = '" + course.getTxtTerm() +
				"' and ddlEdu = '" + course.getDdlEdu() +
				"' and ddlDept = '" + course.getDdlDept() +
				"' and code = '" + course.getCode() + "'";
		return jdbcTemplate.queryForList("SELECT * FROM course where " + condition);
	}
	
	/*
	 * 新增課程至課程資料表
	 */
	public static int insertCourse(Course course, JdbcTemplate jdbcTemplate) {
		String key = "(txtYears,txtTerm,ddlEdu,ddlDept,code)";
		String value =  "('"+ String.join("','", 
						course.getTxtYears(),
						course.getTxtTerm(),
						course.getDdlEdu(),
						course.getDdlDept(),
						course.getCode()) +
						"')";
		int status = jdbcTemplate.update("Insert Into course " + key + "Values" + value);
		return status;
	}
}
