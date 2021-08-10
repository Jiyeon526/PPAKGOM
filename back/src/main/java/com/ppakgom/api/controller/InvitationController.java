package com.ppakgom.api.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

import com.ppakgom.common.auth.SsafyUserDetails;
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserInterest;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.UserRepository;
import com.ppakgom.db.repository.UserStudyRepository;
import com.ppakgom.api.response.InviteGetRes;
import com.ppakgom.api.response.InviteRes;
import com.ppakgom.api.response.StudyCreatePostRes;
import com.ppakgom.api.response.StudyRes;
import com.ppakgom.api.response.StudySearchGetRes;
import com.ppakgom.api.service.StudyApplyService;
import com.ppakgom.api.service.StudyRateService;
import com.ppakgom.api.service.StudyService;
import com.ppakgom.api.service.UserService;
import com.ppakgom.api.service.UserStudyService;
import com.ppakgom.api.service.UserInterestService;
import com.ppakgom.api.request.InviteReq_receiver;
import com.ppakgom.api.request.InviteReq_sender;
import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.request.StudyInvitePostReq;
import com.ppakgom.api.request.StudyRatePostReq;

/**
 * 스터디 초대 관련 API 요청을 처리하는 컨트롤러
 */

@Api(value = "스터디 초대 API", tags = { "Invitation" })
@RestController
@RequestMapping("api/v1/invitation")
public class InvitationController {

	@Autowired
	StudyApplyService studyApplyService;

	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;

	@Autowired
	UserStudyService userStudyService;

	@Autowired
	StudyRateService studyRateService;

	/* 스터디원 초대 현황 */
	@GetMapping("/response/{userId}")
	@ApiOperation(value = "스터디 초대 현황", notes = "로그인한 유저가 초대한 리스트를 보여줍니다.")
	public ResponseEntity<?> getInviteListOfUser(
			@PathVariable(value = "userId") @ApiParam(value = "현재 유저", required = true) Long userId) {

		try {
			InviteGetRes res = new InviteGetRes();
			List<StudyApply> inviteList = studyApplyService.getInviteList(userId);
			for (StudyApply sa : inviteList) {
				InviteRes ir = new InviteRes();
				res.getInviteResult().add(ir.of(sa));
			}

			return ResponseEntity.ok(res);

		} catch (Exception e) {
			BaseResponseBody res = new BaseResponseBody(400, "잘못된 요청");
			return ResponseEntity.status(400).body(res);
		}
	}

	/* 받은 초대 목록 */
	@GetMapping("/request/{userId}")
	@ApiOperation(value = "받은 초대 현황", notes = "로그인한 유저가 받은 초대 목록을 보여줍니다.")
	public ResponseEntity<?> getInvitedListOfUser(
			@PathVariable(value = "userId") @ApiParam(value = "현재 유저", required = true) Long userId) {

		try {
			InviteGetRes res = new InviteGetRes();
			List<StudyApply> inviteList = studyApplyService.getInvitedList(userId, false);
			for (StudyApply sa : inviteList) {
				InviteRes ir = new InviteRes();
				res.getInviteResult().add(ir.of(sa));
			}
			return ResponseEntity.ok(res);

		} catch (Exception e) {
			BaseResponseBody res = new BaseResponseBody(400, "잘못된 요청");
			return ResponseEntity.status(400).body(res);
		}
	}

	/* 초대 취소하기 */
	@DeleteMapping("/response/calcel/{userId}")
	@ApiOperation(value = "초대 취소하기", notes = "로그인한 유저가 보낸 초대를 취소합니다.")
	public ResponseEntity<BaseResponseBody> cancelInvitation(InviteReq_receiver req,
			@PathVariable(value = "userId") @ApiParam(value = "현재 유저", required = true) Long userId) {
		try {
			studyApplyService.cancelInvitation(req, userId, false);
			return ResponseEntity.ok().body(new BaseResponseBody(200, "초대 취소 완료"));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new BaseResponseBody(400, "잘못된 요청"));
		}

	}

	/* 초대 거절하기 -> state를 1로 변경 */
	@PutMapping("/request/reject/{userId}")
	@ApiOperation(value = "초대 거절하기", notes = "자신에게 온 초대를 거절합니다.")
	public ResponseEntity<BaseResponseBody> rejectInvitation(InviteReq_sender req,
			@PathVariable(value = "userId") @ApiParam(value = "현재 유저", required = true) Long userId) {

		try {
			studyApplyService.rejectInvitation(req, userId);
			return ResponseEntity.ok().body(new BaseResponseBody(200, "초대 취소 완료"));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new BaseResponseBody(400, "잘못된 요청"));
		}

	}

	/* 초대 거절 확인 -> DB에서 지우기 */
	@DeleteMapping("/response/reject/{userId}")
	@ApiOperation(value = "초대 거절 확인", notes = "자신이 거절받은 초대를 확인하고, 목록에서 지웁니다.")
	public ResponseEntity<BaseResponseBody> confirmRejected(InviteReq_receiver req,
			@PathVariable(value = "userId") @ApiParam(value = "현재 유저", required = true) Long userId) {
		try {
			studyApplyService.confirmRejectedInvitation(req, userId);
			return ResponseEntity.ok().body(new BaseResponseBody(200, "초대 취소 완료"));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new BaseResponseBody(400, "잘못된 요청"));
		}
	}

	/* 초대를 승인한다 -> 스터디에 새로운 멤버 가입하기. */
	@PostMapping("/request/ok/{userId}")
	@ApiOperation(value = "초대 승인", notes = "자신에게 온 초대를 승낙하고, 새로운 스터디에 가입합니다.")
	public ResponseEntity<BaseResponseBody> agreeInvitation(InviteReq_sender req,
			@PathVariable(value = "userId") @ApiParam(value = "현재 유저", required = true) Long userId) {
//		DB에서 삭제
		try {
			studyApplyService.agreeInvitation(req, userId);
			Study study = studyService.getStudyById(req.getStudyId()).get();
			
//		population vs 현재 인원 확인 -> 마감시 알려주기.
			List<UserStudy> userStudy = userStudyService.getCurrentMember(study.getId());
			if (study.getPopulation() <= userStudy.size()) {
				return ResponseEntity.status(400).body(new BaseResponseBody(400, "이미 마감된 스터디입니다."));
			}

//		평가 목록에 추가
//		내가 다른 스터디원을 평가해야 하고, 다른스터디원들이 나를 평가해야 함
			User newUser = userService.getUserById(userId);
			System.out.println("뉴비 정보 "+newUser);
			studyRateService.addRating(study, userStudy, newUser);
			
//		스터디-회원 테이블에 추가
			userStudyService.addMember(study, newUser);
			return ResponseEntity.ok(new BaseResponseBody(200,"초대 승인 완료"));

		} catch (Exception e) {
			return ResponseEntity.status(400).body(new BaseResponseBody(404, "다시 시도해 주세요."));
		}
	}

}
