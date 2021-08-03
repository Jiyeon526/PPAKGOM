package com.ppakgom.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.api.response.UserRes;
import com.ppakgom.api.service.UserService;
import com.ppakgom.common.auth.SsafyUserDetails;
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/register")
	@ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.") 
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공"),
		@ApiResponse(code = 401, message = "인증 실패"),
		@ApiResponse(code = 404, message = "사용자 없음"),
		@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value="회원가입 정보", required = true) UserRegisterPostReq registerInfo) {

		//임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
		User user = userService.createUser(registerInfo);

		//return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
	}

	@GetMapping("/me")
	@ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.") 
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공"),
		@ApiResponse(code = 401, message = "인증 실패"),
		@ApiResponse(code = 404, message = "사용자 없음"),
		@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<UserRes> getUserInfo(@ApiIgnore Authentication authentication) {
		/**
		 * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
		 * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
		 */
		System.out.println("인증~~~~");
		System.out.println(authentication);
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);
		
		return ResponseEntity.status(200).body(UserRes.of(user));
	}

	/////////////////////////////
	@PatchMapping("/{userId}")
	@ApiOperation(value = "회원 정보 수정", notes = "부서 직책 이름 수정하기")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> updateUserInfo(@ApiIgnore Authentication authentication, @PathVariable  @ApiParam(value="회원수정 정보 대상  ID", required = true) String userId, @RequestBody @ApiParam(value="회원수정 정보", required = true) UserRegisterPostReq registerInfo) {

		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String authUserId = userDetails.getUsername();
		if(authUserId.equals(userId)){
			userService.updateUserInfo(userId, registerInfo);
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		}

		return ResponseEntity.status(401).body(BaseResponseBody.of(401, "Failure"));
	}

	@DeleteMapping("/{userId}")
	@ApiOperation(value = "회원 식제", notes = "아이디를 통한 회원 삭제")
	@ApiResponses({
			@ApiResponse(code = 204, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> deleteUserInfo(@ApiIgnore Authentication authentication, @PathVariable  @ApiParam(value="회원삭제 대상 ID", required = true) String userId) {
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String authUserId = userDetails.getUsername();
		if(authUserId.equals(userId)){
			userService.deleteUserInfo(userId);
			return ResponseEntity.status(204).body(BaseResponseBody.of(204, "Success"));
		}
		return ResponseEntity.status(401).body(BaseResponseBody.of(401, "Failure"));
	}

	@GetMapping("/{userId}")
	@ApiOperation(value = "id 중복 체크", notes = "<strong>id/strong>중복체크")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류"),
			@ApiResponse(code = 409, message = "이미 존재하는 사용자 ID입니다.")
	})
	public ResponseEntity<? extends BaseResponseBody>checkDuplicateId(@PathVariable  @ApiParam(value="중복확인 ID", required = true) String userId) {
		System.out.println(userId);
		boolean flag = userService.checkDuplicateId(userId);
		System.out.println(flag);
		if(flag) {
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 존재하는 사용자 ID입니다."));
		}
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}


}
