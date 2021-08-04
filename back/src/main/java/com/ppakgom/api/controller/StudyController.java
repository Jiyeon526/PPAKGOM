package com.ppakgom.api.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/* spring web */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/* swagger */
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import com.ppakgom.common.auth.SsafyUserDetails;
/* response */
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.common.util.JwtTokenUtil;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;

import com.ppakgom.api.response.LoginRes;
import com.ppakgom.api.response.StudyCreatePostRes;
import com.ppakgom.api.response.StudySearchGetRes;
import com.ppakgom.api.service.StudyService;
import com.ppakgom.api.service.UserService;

/* request */
import com.ppakgom.api.request.LoginReq;
import com.ppakgom.api.request.StudyCreatePostReq;

/**
 * 스터디 CRUD 관련 API 요청을 처리하는 컨트롤러
 */

@Api(value = "스터디 API", tags = { "Study" })
@RestController
@RequestMapping("api/v1/study")
public class StudyController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	StudyService studyService;

	/* 스터디 생성 */
	@PostMapping("/")
	@ApiOperation(value = "스터디 생성", notes = "스터디 명, 마감인원 등을 받으면 스터디를 생성합니다.", consumes = "multipart/form-data", produces = "multipart/form-data")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = LoginRes.class),
			@ApiResponse(code = 404, message = "사용자 없거나 인증 실패", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class), })
	public ResponseEntity<StudyCreatePostRes> createStudy(
			 @ApiParam(value = "로그인 정보", required = true) StudyCreatePostReq studyInfo,
			@RequestPart("study_thumbnail") MultipartFile studyThumbnail, @ApiIgnore Authentication authentication) {
		
		Study study = null;
		
//		아직 로그인-회원가입 연결 안되있어서 여기 안해놓음
//		로그인한 사용자가 owner_id 가 됨.
//		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
//		String userId = userDetails.getUsername();
//		User user = userService.getUserByUserId(userId);
		User user = null;
		
		try {
			
			study = studyService.createStudy(studyInfo, user, studyThumbnail);
			
		} catch (ParseException e) {
			System.err.println("날짜 파싱 에러");
			e.printStackTrace();
			return (ResponseEntity<StudyCreatePostRes>) ResponseEntity.status(500);
		}catch (Exception e) {
			System.err.println("파일 저장 에러");
			e.printStackTrace();
			return (ResponseEntity<StudyCreatePostRes>) ResponseEntity.status(500);
		}
//		성공 응답 -> 아뒤
		return ResponseEntity.ok(new StudyCreatePostRes(study.getId()));
	}
	
	/* 스터디 검색 */
	@GetMapping("/")
	@ApiOperation(value = "스터디 검색", notes = "전체 스터디 목록 검색")
	public ResponseEntity<StudySearchGetRes> searchAllStudy() {
		
		StudySearchGetRes res = new StudySearchGetRes();
		res.setStudyResult(new ArrayList<>());
		
		List<Study> resultSet = studyService.getAllStudy();
		for(Study s : resultSet) {
			res.getStudyResult().add(s);
		}
		return ResponseEntity.ok(res);
	}
}
