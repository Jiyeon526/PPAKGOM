package com.ppakgom.api.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.api.service.StudyService;
import com.ppakgom.api.service.EmailService;
import com.ppakgom.api.service.UserService;
import com.ppakgom.common.auth.SsafyUserDetails;
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;
import com.ppakgom.common.util.JwtTokenUtil;
import com.ppakgom.api.response.LoginRes;
import com.ppakgom.api.response.UserInfoRes;
import com.ppakgom.api.request.EmailReq;
import com.ppakgom.api.request.LoginReq;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.UserStudyRepository;
import com.ppakgom.db.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import springfox.documentation.annotations.ApiIgnore;

import com.ppakgom.api.request.UserModifyInfoReq;
import com.ppakgom.api.response.StudyRes;
import com.ppakgom.api.response.StudySearchGetRes;

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

	@Autowired
	StudyService studyService;

	@Autowired
	StudyInterestRepository studyInterestRepository;

	HashMap<String, String> emailRepository = new HashMap<>();

	@Autowired
	UserStudyRepository userStudyRepository;

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
			@RequestPart(value = "file", required = false) MultipartFile thumbnail) {

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

	/* 찜한 스터디 가져오기 */
	@GetMapping("/like/{userId}")
	@ApiOperation(value = "찜한 스터디", notes = "사용자가 찜한 스터디를 가져온다")
	public ResponseEntity<StudySearchGetRes> searchStudyByUserLike(
			@PathVariable(value = "userId", required = false) Long userId) {

		StudySearchGetRes res = new StudySearchGetRes();
		res.setStudyResult(new ArrayList<>());

		try {

			User user = userService.getUserById(userId);
			List<Study> resultSet = studyService.getUserLikeStudy(user);

//		사용자가 가입한 스터디.
			List<Study> userStudyList = studyService.getUserJoinStudy(user);
			List<Study> userLikedStudy = studyService.getUserLikeStudy(user);

			for (Study s : resultSet) {
				StudyRes studyRes = new StudyRes();
//				찜한 스터디가 가입한 스터디일 경우 pass
				if(userStudyList.contains(s))
					continue;
				res.getStudyResult().add(studyRes.of(s, studyInterestRepository, userStudyRepository, userStudyList, userLikedStudy));
			}
		} catch (Exception e) {
			System.out.println("존재하지 않는 사용자이거나 이외의 에러");
		}

		return ResponseEntity.ok(res);

	}

	@PostMapping("/email")
	@ApiOperation(value = "이메일 인증 코드 보내기", notes = "<strong>이메일</strong>인증 코드 보내기.")
	public ResponseEntity<? extends BaseResponseBody> sendEmailAuth(@RequestBody String email) throws Exception {

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

		User user = userService.getUserByUserId(userDetails.getUsername()); // 사용자 정보
		List<String> interest = userService.getInterest(user.getId()); // 관심사 리스트

		UserInfoRes userInfoRes = UserInfoRes.of(user, interest);

		return ResponseEntity.status(200).body(userInfoRes);
	}

	@PutMapping("/{userId}")
	@ApiOperation(value = "회원 본인 정보 수정", notes = "로그인한 회원 본인의 정보를 수정한다.", consumes = "multipart/form-data", produces = "multipart/form-data")
	public ResponseEntity<? extends BaseResponseBody> modifyUserInfo(UserModifyInfoReq userReq,
			@RequestPart(value = "thumbnail", required = false) MultipartFile file,
			@PathVariable @ApiParam(value = "User ID", required = true) Long userId,
			@ApiIgnore Authentication authentication) {

		User user = userService.getUserById(userId);

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		Long authUserId = userDetails.getUser().getId();

		if (user == null || authUserId == null || user.getId() == null) // 사용자가 없는 경우 or 로그인한 사용자와 현재 사용자가 다른 경우
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
		
		if(authUserId != user.getId())
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
		
		String res = userService.modifyUserInfo(user, userReq, file);
		if("ok".equals(res)) {
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원 정보 수정 완료"));
		}else if("name".equals(res)) {
			return ResponseEntity.status(401).body(BaseResponseBody.of(401, "닉네임을 다시 입력해주세요."));
		}

		return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
	}

	
	@GetMapping("/{name}/profile")
	@ApiOperation(value = "다른 회원 정보 확인", notes = "다른 회원 정보를 확인한다.", consumes = "multipart/form-data", produces = "multipart/form-data")
	public ResponseEntity<UserInfoRes> getUserInfoNotMe(@PathVariable @ApiParam(value = "name", required = true) String name) {
		
		User user = userService.getUserByName(name); // 사용자 정보
		
		if(user == null) return new ResponseEntity<UserInfoRes>(HttpStatus.BAD_REQUEST); // 해당 사용자가 없을 때
		
		List<String> interest = userService.getInterest(user.getId()); // 관심사 리스트

		for (String s : interest) {
			System.out.println(s);
		}

		UserInfoRes userInfoRes = UserInfoRes.of(user, interest);

		return ResponseEntity.status(200).body(userInfoRes);
	}

	/* 가입한 스터디 가져오기 */
	@GetMapping("/join/{userId}")
	@ApiOperation(value = "가입한 스터디", notes = "사용자가 가입한 스터디를 가져온다")
	public ResponseEntity<StudySearchGetRes> searchStudyByUserJoin(
			@PathVariable(value = "userId", required = false) Long userId) {

		StudySearchGetRes res = new StudySearchGetRes();
		res.setStudyResult(new ArrayList<>());

		User user = userService.getUserById(userId);
//		존재하지 않는 사용자일 때
		if (user == null)
			return ResponseEntity.ok(res);
		List<Study> resultSet = studyService.getUserJoinStudy(user);
		List<Study> userLikedStudy = studyService.getUserLikeStudy(user);
		
		for (Study s : resultSet) {
			StudyRes studyRes = new StudyRes();
			res.getStudyResult().add(studyRes.of(s, studyInterestRepository, userStudyRepository, resultSet, userLikedStudy));
		}

		return ResponseEntity.ok(res);

	}

	/* 스터디 찜하기 */
	@PostMapping("/like/{userId}")
	@ApiOperation(value = "스터디 찜하기", notes = "관심있는 스터디를 찜한다.")
	public ResponseEntity<BaseResponseBody> likeStudy(@PathVariable(value = "userId", required = true) Long userId,
			Long studyId) {

		try {

			User user = userService.getUserById(userId);
			Optional<Study> studyTmp = studyService.getStudyById(studyId);
			Study study = null;
			if(studyTmp.isPresent())
				study = studyTmp.get();
			if(study == null)
				return ResponseEntity.status(400).body(new BaseResponseBody(400, "다시 시도해 주세요."));
			
			userService.likeStudy(user, study);

			return ResponseEntity.ok(new BaseResponseBody(200, "찜하기 완료"));

		} catch (Exception e) {
			return ResponseEntity.status(400).body(new BaseResponseBody(400, "다시 시도해 주세요."));
		}

	}

	/* 스터디 찜 취소 */
	@DeleteMapping("/like/{userId}")
	@ApiOperation(value = "스터디 찜하기 취소", notes = "찜했던 스터디를 취소한다.")
	public ResponseEntity<BaseResponseBody> unlikeStudy(@PathVariable(value = "userId", required = true) Long userId,
			Long studyId) {

		try {
			User user = userService.getUserById(userId);
			Optional<Study> studyTmp = studyService.getStudyById(studyId);
			Study study = null;
			if(studyTmp.isPresent())
				study = studyTmp.get();
			if(study == null) {
				return ResponseEntity.status(400).body(new BaseResponseBody(400, "다시 시도해 주세요."));
			}
			userService.unlikeStudy(user, study);

			return ResponseEntity.ok(new BaseResponseBody(200, "찜하기 취소 완료"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(new BaseResponseBody(400, "다시 시도해 주세요."));
		}

	}

	/* 가입한 스터디 목록 -> 이름으로 ,이름만 */
	@GetMapping("/join/name/{userName}")
	@ApiOperation(value = "가입한 스터디 목록", notes = "가입했던 스터디 목록")
	public ResponseEntity<?> getStudyByName(@PathVariable(value = "userName", required = true) String userName) {

		User user = userService.getUserByUserNickname(userName).orElse(null);

		if (user == null)
			return ResponseEntity.status(404).body(new BaseResponseBody(404, "존재하지 않는 사용자"));

		List<String> study = new ArrayList<String>();

		List<Study> resultSet = studyService.getUserJoinStudy(user);

		try {
			for (Study s : resultSet) {
				study.add(s.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(study);
	}

	
	@PostMapping("/social")
	@ApiOperation(value = "소셜 로그인", notes = "소셜 로그인")
	public ResponseEntity<?> userSocialLogin(@RequestBody @ApiParam(value = "로그인 정보", required = true) String email) {
		
		User user;
		BaseResponseBody loginRes;
		
		// loginInfo가 다 안왔을 때 400에러
		loginRes = new BaseResponseBody(404, "아이디 또는 비밀번호를 확인해 주세요.");
		
		if(email == null) return ResponseEntity.status(404).body(loginRes); // 빈값
		
		try {
//			존재하는 사용자이면 패쓰
			user = userService.getUserByEmail(email);
			
			if(user != null) {
				loginRes = new LoginRes(200, "로그인 완료", JwtTokenUtil.getToken(user.getUserId()), user.getId());
			} else { // 아이디 없을 때 
				user = userService.postSocialLoginInfo(email);
				if(user != null) // 가입되면
					loginRes = new LoginRes(200, "로그인 완료", JwtTokenUtil.getToken(user.getUserId()), user.getId());
				else // 가입 실패
					return ResponseEntity.status(404).body(loginRes);
			}
			return ResponseEntity.ok(loginRes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.status(404).body(loginRes);
	}
	
	/* 프로필 다운로드 */
	
//	스터디 썸네일
	@GetMapping("profile/{file}/download")
	@ApiOperation(value = "파일 경로", notes = "<strong>이미지</strong>를 다운로드 한다.")
	public void downloadProfile(@PathVariable(value = "file") @ApiParam(value = "파일경로", required = true) String file, HttpServletResponse response) throws UnsupportedEncodingException {
	    System.err.println(file);
	    
	    String path = "/image/";
	    if(!"default.png".equals(file))
	    	path += "user/";
	    path += file;
	    
	    String fileNm = file;
	    StringBuffer sb = new StringBuffer(); 
	    for (int i = 0; i < fileNm.length(); i++) 
	    { 
	        char c = fileNm.charAt(i); 
	        if (c > '~') 
	        { 
	            sb.append(URLEncoder.encode(Character.toString(c), "UTF-8")); 
	        } else { 
	            sb.append(c); 
	        } 
	    } 
	    String reFileNm = sb.toString();    
	    response.setContentType("application/octet-stream; charset=UTF-8");// 이번 응답은 html이 아니라 파일이다.
	    response.setHeader("Content-Disposition", "attachment; filename=\""+reFileNm+"\"");
	    response.setHeader("Content-Transfer-Encoding", "binary");
	    try (FileInputStream is =new FileInputStream(path); ){
	        
	        ServletOutputStream os = response.getOutputStream();
	        
	        int data = 0;
	        while((data=is.read())!= -1)
	            os.write(data);
	        
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	}
}
