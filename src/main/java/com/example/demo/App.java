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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.database.CourseDB;
import com.example.demo.database.NoticeDB;
import com.example.demo.database.RoleDB;
import com.example.demo.database.UserDB;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;


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
		String logintoken = request.getUser().login(jwtTokenProvider,jdbcTemplate);
	    ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
	    objectNode.put("loginToken", logintoken);
	    return objectNode;
	}
	
	@RequestMapping(value = "/api/auth/register", method = RequestMethod.POST)
	public String userRegister(@RequestBody HTTPRequestBody request) throws IOException {
		request.getUser().register(mailSender,jwtTokenProvider,jdbcTemplate);
		return "register";
	}
	
	@RequestMapping(value = "/api/auth/verify/{verifyToken}", method = RequestMethod.GET)
	public String userVerify(@PathVariable String verifyToken) throws IOException {
		Jws<Claims> claims = jwtTokenProvider.getClaim(verifyToken);
		/*���o�ϥΪ̸��(�W�١B����)*/
		int id = (int) claims.getBody().get("id");
		List<Map<String, Object>> userList = UserDB.serachUserByID(id, jdbcTemplate);
		if(userList.size()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"user is not found.");
		
		boolean IsVerify = (boolean) userList.get(0).get("IsVerify");
		if(IsVerify) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"has already verify.");
		
		//�s�W�ϥΪ̨���
		int status = RoleDB.insertUserRole(id,1,jdbcTemplate);
		if(status<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong with add user role.");
		
		UserDB.updateVerifyTag((int) claims.getBody().get("id"),verifyToken,jdbcTemplate);
		return "verify";
	}
	
	@RequestMapping("/users")
	public String getUser() throws IOException {
//		CourseHelper.getCourseRemainingNums("109",1,1,1,"AAR00690");
		List rows = jdbcTemplate.queryForList("SELECT * FROM member");
		return rows.toString();
	}
	
	@RequestMapping(value = "/requesCN", method = RequestMethod.POST)
	public String requesCourseNotice(@RequestBody Course request) {
		/*�d�߽ҵ{�M��O�_�w�s�b*/
		List<Map<String, Object>> rows = CourseDB.searchCourse(request,jdbcTemplate);
		System.out.print(rows.size());
		/*�Y�L�h�s�J�ҵ{�M��*/
		if(rows.size() <= 0) {
			/*���ҽҵ{�O�_�s�b��Ǯ�*/
			
			int status = CourseDB.insertCourse(request,jdbcTemplate);
			System.out.print(status);
			rows = CourseDB.searchCourse(request,jdbcTemplate);
		}
		
		int courseID = (int) rows.get(0).get("id");
		int userID = 1;
		System.out.print(rows.get(0).get("id"));
		
		/*���ҽҵ{�ݳq���O�_�s�b*/
		rows = NoticeDB.searchWaitingCourseNoticeByUser(courseID, userID, jdbcTemplate);
		System.out.print(rows.size());
	
		/*�s�W�q���M��*/
		if(rows.size() <= 0) {
			int status = NoticeDB.insertCourseNotice(courseID, userID, jdbcTemplate);
			System.out.print(status);
		}
		return request.toString();
	}
}
