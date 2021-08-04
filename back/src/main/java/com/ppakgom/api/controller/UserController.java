package com.ppakgom.api.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

/* spring web */
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* swagger */
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/* response */
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.common.util.JwtTokenUtil;
import com.ppakgom.db.entity.User;
import com.ppakgom.api.response.LoginRes;
import com.ppakgom.api.service.UserService;

/* request */
import com.ppakgom.api.request.LoginReq;

/**
 * 회원 CRUD 관련 API 요청을 처리하는 컨트롤러
 */

@Api(value="회원 API", tags = {"User"})
@RestController
@RequestMapping("api/v1/users")
public class UserController {

	@Autowired 
	UserService userService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
/* 로그인 */
	@PostMapping("/login")
	@ApiOperation(value="jwt 로그인", notes="아이디와 패스워드를 받으면 로그인 성공 여부를 반환한다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = LoginRes.class),
		@ApiResponse(code = 404, message = "사용자 없거나 인증 실패", response = BaseResponseBody.class),
		@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class),
	})
	public ResponseEntity<LoginRes> login(@RequestBody @ApiParam(value="로그인 정보", required = true) LoginReq loginInfo) {
		
		User user;
		LoginRes loginRes = new LoginRes();
		
		try {
//			존재하는 사용자이고
			user = userService.getUserByUserId(loginInfo.getId());

//			비밀번호가 맞는다면
			if(passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
				loginRes.setAccessToken(JwtTokenUtil.getToken(user.getUser_id()));
				loginRes.setStatusCode(200);
				loginRes.setMessage("로그인 완료");
				return ResponseEntity.ok(loginRes);
			}else {
				loginRes.setStatusCode(404);
				loginRes.setMessage("아이디 또는 비밀번호를 확인해주세요.");
				return ResponseEntity.status(404).body(loginRes);
			}
			
//		아이디 없을 때 
		} catch(NoSuchElementException e ) { 
			e.printStackTrace();
			loginRes.setStatusCode(404);
			loginRes.setMessage("아이디 또는 비밀번호를 확인해주세요.");
			return ResponseEntity.status(404).body(loginRes);

//			서버 문제
		} catch(Exception e) {
			loginRes.setStatusCode(500);
			loginRes.setMessage("서버 오류");
			return ResponseEntity.status(500).body(loginRes);
		}
		
	
	}
}
