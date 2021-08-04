package com.ppakgom.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.api.service.UserService;
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	@ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.")
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value="회원가입 정보", required = true) UserRegisterPostReq registerInfo,
			@RequestPart("thumbnail") MultipartFile thumbnail ) {

		/* 임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음. */
		User user = userService.createUser(registerInfo, thumbnail);
		
		
		if(user == null) // 회원가입이 실패한 경우
			return ResponseEntity.status(201).body(BaseResponseBody.of(400, "다시 시도해주세요."));
		
		/* 회원가입 성공한 경우 */
		return ResponseEntity.status(201).body(BaseResponseBody.of(201, "가입 승인 완료"));
	}
}
