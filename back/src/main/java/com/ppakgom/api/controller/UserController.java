package com.ppakgom.api.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.api.service.EmailService;
import com.ppakgom.api.service.UserService;
import com.ppakgom.common.auth.SsafyUserDetails;
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.User;
import com.ppakgom.common.util.JwtTokenUtil;
import com.ppakgom.api.response.LoginRes;
import com.ppakgom.api.response.UserInfoRes;
import com.ppakgom.api.request.EmailReq;
import com.ppakgom.api.request.LoginReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import springfox.documentation.annotations.ApiIgnore;


/**
 * 회원 CRUD 관련 API 요청을 처리하는 컨트롤러
 */

@Api(value = "회원 API", tags = { "User" })
@RestController
@RequestMapping("api/v1/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@Autowired
	PasswordEncoder passwordEncoder;

	HashMap<String, String> emailRepository = new HashMap<>();

	/* 로그인 */
	@PostMapping("/login")
	@ApiOperation(value = "jwt 로그인", notes = "아이디와 패스워드를 받으면 로그인 성공 여부를 반환한다.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = LoginRes.class),
			@ApiResponse(code = 404, message = "사용자 없거나 인증 실패", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class), })
	public ResponseEntity<?> login(@RequestBody @ApiParam(value = "로그인 정보", required = true) LoginReq loginInfo) {

		User user;
		BaseResponseBody loginRes;

		try {
//			존재하는 사용자이고
			user = userService.getUserByUserId(loginInfo.getId());
//			비밀번호가 맞는다면
			if (passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
				loginRes = new LoginRes(200, "로그인 완료", JwtTokenUtil.getToken(user.getUserId()), user.getId());
				return ResponseEntity.ok(loginRes);
			} else {
				loginRes = new BaseResponseBody(404, "아이디 또는 비밀번호를 확인해 주세요.");
				return ResponseEntity.status(404).body(loginRes);
			}

//		아이디 없을 때 
		} catch (Exception e) {
			e.printStackTrace();
			loginRes = new BaseResponseBody(404, "아이디 또는 비밀번호를 확인해 주세요.");
			return ResponseEntity.status(404).body(loginRes);
		}
	}

	@PostMapping("/register")
	@ApiOperation(value = "회원가입 생성", notes = "회원가입", consumes = "multipart/form-data", produces = "multipart/form-data")
	public ResponseEntity<? extends BaseResponseBody> register(
			@ApiParam(value = "생성할 방 정보", required = true) UserRegisterPostReq registerInfo,
			@RequestPart("file") MultipartFile thumbnail) {

		/*
		 * 임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
		 */
		User user = userService.createUser(registerInfo, thumbnail);

		if (user == null) // 회원가입이 실패한 경우
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해주세요."));

		/* 회원가입 성공한 경우 */
		return ResponseEntity.status(201).body(BaseResponseBody.of(201, "가입 승인 완료"));
	}

	@GetMapping("/{nickname}/name")
	@ApiOperation(value = "닉네임 중복 체크", notes = "<strong>닉네임</strong>중복 체크를 한다.")
	public ResponseEntity<? extends BaseResponseBody> checkDuplicateName(
			@PathVariable(value = "nickname") @ApiParam(value = "중복확인 Name", required = true) String nickname) {

		// 만약 해당 닉네임이 있다면 400에러 발생
		if (!userService.checkName(nickname))
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "이미 존재하는 닉네임입니다."));

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "사용 가능한 닉네임입니다."));
	}

	@GetMapping("/{userid}/userid")
	@ApiOperation(value = "아이디 중복 체크", notes = "<strong>아이디</strong>중복 체크를 한다.")
	public ResponseEntity<? extends BaseResponseBody> checkDuplicateId(
			@PathVariable(value = "userid") @ApiParam(value = "중복확인 ID", required = true) String userid) {

		// 만약 해당 아이디가 있다면 400에러 발생
		User user = userService.getUserByUserId(userid);
		if (user != null)
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "이미 존재하는 아이디입니다."));

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "사용 가능한 아이디입니다."));
	}

	@PostMapping("/email")
	@ApiOperation(value = "이메일 인증 코드 보내기", notes = "<strong>이메일</strong>인증 코드 보내기.")
	public ResponseEntity<? extends BaseResponseBody> sendEmailAuth(String email) throws Exception {

		String code = emailService.sendAuthMessage(email);
		emailRepository.put(email, code); // 해당 사용자의 인증 코드 저장

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "이메일 인증 코드를 보냈습니다."));
	}

	@PostMapping("/verifyCode")
	@ApiOperation(value = "이메일 인증 코드 검증", notes = "<strong>이메일</strong>인증 코드 검증하기.")
	public ResponseEntity<? extends BaseResponseBody> verifyEmailCode(@RequestBody EmailReq emailReq) {

		if (emailReq.getCode().length() != 0 && emailReq.getCode().equals(emailRepository.get(emailReq.getEmail()))) {
			emailRepository.remove(emailReq.getEmail());
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "이메일 인증이 완료되었습니다."));
		}

		return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
	}

	@GetMapping("/me")
	@ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.")
	public ResponseEntity<UserInfoRes> getUserInfo(@ApiIgnore Authentication authentication) {
		// 로그인된 사용자 정보 받아오기
		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();

		User user = userService.getUserByUserId(userDetails.getUsername());
		List<String> interest = userService.getInterest(user.getId());

		System.out.println(
				user.getId() + " " + user.getName() + " " + user.getEmail() + " " + user.getProfile_thumbnail());
		for (String s : interest) {
			System.out.println(s);
		}

		UserInfoRes userInfoRes = UserInfoRes.of(user, interest);

		return ResponseEntity.status(200).body(userInfoRes);
	}
}
