package com.ppakgom.api.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/* spring web */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.UserStudyRepository;
import com.ppakgom.api.response.LoginRes;
import com.ppakgom.api.response.StudyCreatePostRes;
import com.ppakgom.api.response.StudyJoinApplyListRes;
import com.ppakgom.api.response.StudyRes;
import com.ppakgom.api.response.StudySearchGetRes;
import com.ppakgom.api.service.JoinService;
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
	
	@Autowired
	StudyInterestRepository studyInterestRepository;
	
	@Autowired
	UserStudyRepository userStudyRepository;
	
	@Autowired
	JoinService joinService;
	
	private final StudyRes STUDY_RES = new StudyRes();
	
	/* 스터디 생성 */
	@PostMapping("/")
	@ApiOperation(value = "스터디 생성", notes = "스터디 명, 마감인원 등을 받으면 스터디를 생성합니다.", consumes = "multipart/form-data", produces = "multipart/form-data")
	public ResponseEntity<StudyCreatePostRes> createStudy(
			@ApiParam(value = "로그인 정보", required = true) StudyCreatePostReq studyInfo,
			@RequestPart("study_thumbnail") MultipartFile studyThumbnail, @ApiIgnore Authentication authentication) {

		Study study = null;

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);
		
		try {

			study = studyService.createStudy(studyInfo, user, studyThumbnail);

		} catch (ParseException e) {
			System.err.println("날짜 파싱 에러");
			e.printStackTrace();
			return (ResponseEntity<StudyCreatePostRes>) ResponseEntity.status(500);
		} catch (Exception e) {
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
	public ResponseEntity<StudySearchGetRes> searchStudyById(@RequestParam(required = false) Long studyId,
			@RequestParam(required = false) String name, @RequestParam(required = false) String interest) {

		StudySearchGetRes res = new StudySearchGetRes();
		res.setStudyResult(new ArrayList<>());
		List<Study> resultSet = new ArrayList<>();
		Optional<Study> study;

//		스터디 전체 검색
		if (studyId == null && name == null && interest == null)
			resultSet = studyService.getAllStudy();

//		아이디로 검색
		if (studyId != null) {
			study = studyService.getStudyById(studyId);
			resultSet.add(study.orElse(null));
		}

//		스터디명으로 검색
		if (name != null) {
			resultSet = studyService.getStudyByName(name);
		}

//		관심사로 검색
		if (interest != null) {
			resultSet = studyService.getStudyByInterest(interest);
		}

		/* 검색 결과 삽입 */
		for (Study s : resultSet) {
			StudyRes sr = STUDY_RES.of(s, studyInterestRepository, userStudyRepository);
			res.getStudyResult().add(sr);
		}
		return ResponseEntity.ok(res);

	}
	
	@GetMapping("/{studyId}/joinlist")
	@ApiOperation(value = "스터디 내에서 가입 요청 리스트 가져오기", notes = "스터디 내에서 가입 요청 리스트 가져오기")
	public ResponseEntity<List<StudyJoinApplyListRes>> studyJoinApplyListRes(@PathVariable Long studyId) {
		
		List<StudyJoinApplyListRes> res = joinService.getStudyJoinApplyList(studyId);
		return ResponseEntity.status(200).body(res);
	}
}
