package com.example.demo.database;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.demo.Course;

public class NoticeDB {

	private NoticeDB() {
		
	}
	
	/*
	 * �d�߽ҵ{�q���O�_�w�s�b��q����ƪ�
	 */
	public static List<Map<String, Object>> searchWaitingCourseNoticeByUser(int courseID, int userID, JdbcTemplate jdbcTemplate) {
		String condition = "Course_ID = " + courseID + 
						" and User_ID = " + userID + 
						" and Status = 'waiting'";
		return jdbcTemplate.queryForList("SELECT * FROM notice where " + condition);
	}
	
	/*
	 * �d�ߥ����ݪ��ҵ{�q����ƪ�
	 */
	public static List<Map<String, Object>> searchAllWaitingCourseNotice(JdbcTemplate jdbcTemplate) {
		String condition = "Status = 'waiting'";
		return jdbcTemplate.queryForList("SELECT * FROM notice where " + condition);
	}
	
	/*
	 * �s�W�ҵ{�q���ܳq����ƪ�
	 */
	public static int insertCourseNotice(int courseID, int userID, JdbcTemplate jdbcTemplate) {
		String key = "(Course_ID, User_ID)";
		String value =  "("+ String.join(",", String.valueOf(courseID), String.valueOf(userID)) + ")";
		int status = jdbcTemplate.update("Insert Into notice " + key + "Values" + value);
		return status;
	}
	
	/*
	 * ��s�ҵ{�q�����A������
	 */
	public static int updateCourseNoticeToFinished(String noticeID, JdbcTemplate jdbcTemplate) {
		String condition = "ID = '" + noticeID + "'";
		String set = "Status = 'finished'";
		int status = jdbcTemplate.update("UPDATE notice Set " + set + " Where " + condition);
		return status;
	}
}
