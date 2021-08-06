package com.ppakgom.api.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserInterest;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.UserStudyRepository;
import com.ppakgom.api.response.LoginRes;
import com.ppakgom.api.response.StudyCreatePostRes;
import com.ppakgom.api.response.StudyRes;
import com.ppakgom.api.response.StudySearchGetRes;
import com.ppakgom.api.service.StudyService;
import com.ppakgom.api.service.UserService;
import com.ppakgom.api.service.UserInterestService;
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
	UserInterestService userInterestService;

	private final StudyRes STUDY_RES = new StudyRes();

	/* 스터디 생성 */
	@PostMapping("/")
	@ApiOperation(value = "스터디 생성", notes = "스터디 명, 마감인원 등을 받으면 스터디를 생성합니다.", consumes = "multipart/form-data", produces = "multipart/form-data")
	public ResponseEntity<?> createStudy(
			@ApiParam(value = "로그인 정보", required = true) StudyCreatePostReq studyInfo,
			@RequestPart(value = "study_thumbnail", required = false) MultipartFile studyThumbnail,
			@ApiIgnore Authentication authentication) {

		
		Study study = null;

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);

		try {

			study = studyService.createStudy(studyInfo, user, studyThumbnail);

		} catch (ParseException e) {
			System.err.println("날짜 파싱 에러");
			e.printStackTrace();
			BaseResponseBody res = new BaseResponseBody(500, "서버 에러");
			return ResponseEntity.status(500).body(res);
		} catch (Exception e) {
			System.err.println("파일 저장 에러");
			e.printStackTrace();
			BaseResponseBody res = new BaseResponseBody(500, "서버 에러");
			return ResponseEntity.status(500).body(res);

		}
//		성공 응답 -> 아뒤
		StudyCreatePostRes res= new StudyCreatePostRes(study.getId());
		return ResponseEntity.ok(res);
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

	/* 사용자 관심 스터디 불러오기 */
	@GetMapping("/interest/{userId}")
	@ApiOperation(value = "관심사 기반 스터디 검색", notes = "사용자 관심사 기반 스터디 검색")
	public ResponseEntity<StudySearchGetRes> searchStudyByUserInterest(
			@PathVariable(value = "userId") @ApiParam(value = "사용자 ID", required = true) Long userId) {
		StudySearchGetRes res = new StudySearchGetRes();
		res.setStudyResult(new ArrayList<>());
		// 사용자의 관심사들에 매칭된 스터디가 겹칠 경우.
		// 예: 관심사: 면접, 대기업이고 한 스터디 관심사도 면접, 대기업 인 경우 해당 스터디가 두 번삽입되는 문제 방지.
		HashSet<Study> tmp = new HashSet<>();

//		1. 사용자 관심사 불러오기.
		User user = userService.getUserById(userId);
		List<UserInterest> userInterest = userInterestService.getInterestByUser(user);
//		2. 관심사에 맞는 스터디 불러오기
		List<Study> resultSet;
//		관심사 있는지부터 체크
		if (userInterest != null) {
			for (UserInterest ui : userInterest) {
//				최대 3개만 저장스
				resultSet = studyService.getStudyByInterest(ui.getInterest().getName());
				for (Study s : resultSet) {
					tmp.add(s);
					if (tmp.size() == 3)
						break;
				}
				if (tmp.size() == 3)
					break;
			}
		}

		/* 검색 결과 삽입 */
		for (Study s : tmp) {
			StudyRes sr = STUDY_RES.of(s, studyInterestRepository, userStudyRepository);
			res.getStudyResult().add(sr);
		}

		return ResponseEntity.ok(res);

	}
	
	
	/* 스터디 상세 정보 불러오기 */
	@GetMapping("/{studyId}/detail")
	@ApiOperation(value = "스터디 상세 정보 조회", notes = "방장 id를 포함한 상세 정보 조회")
	public ResponseEntity<StudySearchGetRes> getStudyDetail(@PathVariable(value = "studyId") @ApiParam(value = "스터디 ID", required = true) Long studyId) {
		
		StudySearchGetRes res = new StudySearchGetRes();
		res.setStudyResult(new ArrayList<>()); //배열로 안줘도 되는데 내가 배열로 준다고 해버려서 ... 추후 논의쓰
		
		Optional<Study> study = studyService.getStudyById(studyId);
		if(study.isPresent()) {
			res.getStudyResult().add(new StudyRes().of(study.get(), studyInterestRepository, userStudyRepository));
		}
		return ResponseEntity.ok(res);
		
	}


	
	
}
