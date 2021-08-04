package com.ppakgom.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해주세요."));
		
		/* 회원가입 성공한 경우 */
		return ResponseEntity.status(201).body(BaseResponseBody.of(201, "가입 승인 완료"));
	}
	
	@GetMapping("/{nickname}/name")
	@ApiOperation(value = "닉네임 중복 체크", notes = "<strong>닉네임</strong>중복 체크를 한다.")
	public ResponseEntity<? extends BaseResponseBody> checkDuplicateName(
			@PathVariable(value = "nickname") @ApiParam(value="중복확인 Name", required = true) String nickname){
		
		// 만약 해당 닉네임이 있다면 400에러 발생
		if(!userService.checkName(nickname))
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "이미 존재하는 닉네임입니다."));
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "사용 가능한 닉네임입니다."));
	}
	
	@GetMapping("/{userid}/userid")
	@ApiOperation(value = "아이디 중복 체크", notes = "<strong>아이디</strong>중복 체크를 한다.")
	public ResponseEntity<? extends BaseResponseBody> checkDuplicateId(
			@PathVariable(value = "userid") @ApiParam(value="중복확인 ID", required = true) String userid){
		
		// 만약 해당 아이디가 있다면 400에러 발생
		User user = userService.getUserByUserId(userid);
		if(user != null)
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "이미 존재하는 아이디입니다."));
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "사용 가능한 아이디입니다."));
	}
}
