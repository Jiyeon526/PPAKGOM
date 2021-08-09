package com.ppakgom.api.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.UserRepository;
import com.ppakgom.db.repository.UserStudyRepository;
import com.ppakgom.api.response.InviteGetRes;
import com.ppakgom.api.response.InviteRes;
import com.ppakgom.api.response.StudyCreatePostRes;
import com.ppakgom.api.response.StudyRes;
import com.ppakgom.api.response.StudySearchGetRes;
import com.ppakgom.api.service.StudyApplyService;
import com.ppakgom.api.service.StudyService;
import com.ppakgom.api.service.UserService;
import com.ppakgom.api.service.UserInterestService;
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

	/* 스터디원 초대 현황 */
	@GetMapping("/response/{userId}")
	@ApiOperation(value = "스터디 초대 현황", notes = "로그인한 유저가 초대한 리스트를 보여줍니다.")
	public ResponseEntity<?> inviteList(
			@PathVariable(value = "userId") @ApiParam(value = "현재 유저", required = true) Long userId) {

		try {
			InviteGetRes res = new InviteGetRes();
			List<StudyApply> inviteList = studyApplyService.getInviteList(userId);
			for(StudyApply sa : inviteList) {
				InviteRes ir = new InviteRes();
				res.getInviteResult().add(ir.of(sa));
			}
			
			return ResponseEntity.ok(res);

		} catch (Exception e) {
			BaseResponseBody res = new BaseResponseBody(400,"잘못된 요청");
			return ResponseEntity.status(400).body(res);
		}
	}

}
