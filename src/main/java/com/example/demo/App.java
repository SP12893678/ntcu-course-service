package com.example.demo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.database.CourseDB;
import com.example.demo.database.NoticeDB;
import com.example.demo.database.UserDB;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


@RestController
public class App {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    private JavaMailSender mailSender;
	
	@RequestMapping("/")
	public String index() {
		return "<h1>Welecome to listeningtrain platform service</h1>";
	}
	
	@RequestMapping(value = "/api/auth/login", method = RequestMethod.POST)
	public ObjectNode userLogin(@RequestBody HTTPRequestBody request) throws IOException {
//		jwtTokenProvider.validateToken(request.getLoginToken());
		String logintoken = UserDB.login(request.getUser(),jwtTokenProvider,jdbcTemplate);
	    ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
	    objectNode.put("loginToken", logintoken);
	    return objectNode;
	}
	
	@RequestMapping(value = "/api/auth/register", method = RequestMethod.POST)
	public String userRegister(@RequestBody HTTPRequestBody request) throws IOException {
		UserDB.register(request.getUser(),mailSender,jwtTokenProvider,jdbcTemplate);
		return "register";
	}
	
	@RequestMapping("/users")
	public String getUser() throws IOException {
//		CourseHelper.getCourseRemainingNums("109",1,1,1,"AAR00690");
		List rows = jdbcTemplate.queryForList("SELECT * FROM member");
		return rows.toString();
	}
	
	@RequestMapping(value = "/requesCN", method = RequestMethod.POST)
	public String requesCourseNotice(@RequestBody Course request) {
		/*查詢課程清單是否已存在*/
		List<Map<String, Object>> rows = CourseDB.searchCourse(request,jdbcTemplate);
		System.out.print(rows.size());
		/*若無則存入課程清單*/
		if(rows.size() <= 0) {
			/*驗證課程是否存在於學校*/
			
			int status = CourseDB.insertCourse(request,jdbcTemplate);
			System.out.print(status);
			rows = CourseDB.searchCourse(request,jdbcTemplate);
		}
		
		int courseID = (int) rows.get(0).get("id");
		int userID = 1;
		System.out.print(rows.get(0).get("id"));
		
		/*驗證課程待通知是否存在*/
		rows = NoticeDB.searchWaitingCourseNoticeByUser(courseID, userID, jdbcTemplate);
		System.out.print(rows.size());
	
		/*新增通知清單*/
		if(rows.size() <= 0) {
			int status = NoticeDB.insertCourseNotice(courseID, userID, jdbcTemplate);
			System.out.print(status);
		}
		return request.toString();
	}
}
