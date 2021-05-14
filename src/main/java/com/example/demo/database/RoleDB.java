package com.example.demo.database;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class RoleDB {

	public static List<Map<String, Object>> getRoleByID(int roleID, JdbcTemplate jdbcTemplate) {
		String condition = "ID = " + roleID;
		return jdbcTemplate.queryForList("SELECT * FROM role where " + condition);
	}
	
	public static int insertUserRole(int userID,int roleID, JdbcTemplate jdbcTemplate) {
		String key = "(UserID,RoleID)";
		String value =  "('"+ String.join("','", 
						String.valueOf(userID),
						String.valueOf(roleID)) +
						"')";
		int status = jdbcTemplate.update("Insert Into `member-role` " + key + "Values" + value);
		return status;
	}
}
