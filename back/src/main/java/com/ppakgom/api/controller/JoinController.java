package com.ppakgom.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ppakgom.api.request.JoinApplyReq;
import com.ppakgom.api.response.JoinApplyListRes;
import com.ppakgom.api.service.JoinService;
import com.ppakgom.api.service.UserService;
import com.ppakgom.api.service.UserStudyService;
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.UserStudy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Api(value="가입 API", tags = {"Join"})
@RestController
@RequestMapping("api/v1/join")
public class JoinController {
	
	@Autowired
	JoinService joinService;
	
	@Autowired
	UserStudyService userStudyService;
	
	@GetMapping("/response/{user_id}")
	@ApiOperation(value="가입 신청 현황", notes="사용자의 가입 신청 현황")
	public ResponseEntity<List<JoinApplyListRes>> joinApplyList(@PathVariable Long user_id) {
		
		List<JoinApplyListRes> res = joinService.getJoinApplyList(user_id);
					
		return ResponseEntity.status(200).body(res);
	}
	
	@DeleteMapping("/response/cancel/{userid}")
	@ApiOperation(value="가입 신청 취소", notes="사용자의 가입 신청 취소")
	public ResponseEntity<? extends BaseResponseBody> joinApplyCancel(@PathVariable Long userid, 
			@RequestBody JoinApplyReq joinApplyReq) {
		
		// 해당 study_apply 정보 가져오기(어차피 하나밖에 없다)
		StudyApply studyApply = joinService.getStudyApply(userid, joinApplyReq);
		
		if(studyApply == null)
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
		
		joinService.deleteJoinApply(studyApply); // 해당 튜플 삭제
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "가입 취소 완료"));

	}
	
	@DeleteMapping("/response/reject/{userid}")
	@ApiOperation(value="가입 신청 거절 결과", notes="사용자의 가입 신청 거절 결과")
	public ResponseEntity<? extends BaseResponseBody> joinApplyReject(@PathVariable Long userid, 
			@RequestBody JoinApplyReq joinApplyReq) {
		
		// 해당 study_apply 정보 가져오기(어차피 하나밖에 없다)
		StudyApply studyApply = joinService.getStudyApply(userid, joinApplyReq);
		
		if(studyApply == null)
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
		
		joinService.deleteJoinApply(studyApply); // 해당 튜플 삭제
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "가입이 거절됐습니다."));
	}
	
	@GetMapping("/request/{userid}")
	@ApiOperation(value="가입 요청 현황", notes="사용자의 가입 요청 현황")
	public ResponseEntity<List<JoinApplyListRes>> joinApplyOwnerList(@PathVariable Long userid) {
		
		List<JoinApplyListRes> res = joinService.getJoinApplyOwnerList(userid);
					
		return ResponseEntity.status(200).body(res);
	}
	
	@PostMapping("/request/ok/{userid}")
	@ApiOperation(value="가입 승인", notes="사용자의 가입 승인")
	public ResponseEntity<? extends BaseResponseBody> joinApplyOk(@PathVariable Long userid
			, @RequestBody JoinApplyReq joinApplyReq) {

		// 해당 study_apply 정보 가져오기(어차피 하나밖에 없다)
		StudyApply studyApply = joinService.getStudyApplyReceiver(userid, joinApplyReq);

//		이미 가입한 사용자면 PASS
		UserStudy userStudy = userStudyService.findUserStudyByUserIdAndStudyId(joinApplyReq.getUser_id(), joinApplyReq.getStudy_id()).orElse(null);
		
		if(userStudy != null)
			return ResponseEntity.status(201).body(BaseResponseBody.of(201, "이미 가입이 완료된 사용자입니다."));
		
		if(studyApply == null)
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
		
		String res = joinService.studyApplyOk(studyApply);
		if(res.equals("population")) { // 인원초과일 경우
			return ResponseEntity.status(201).body(BaseResponseBody.of(201, "인원 초과입니다."));
		} else if(res.equals("ok")) { // 가입 승인 완료
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "가입 승인 완료"));
		} else { // 그 외
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
		}

	}
	
	@PutMapping("/request/reject/{userid}")
	@ApiOperation(value="방장이 가입 거절", notes="방장이 가입 거절함")
	public ResponseEntity<? extends BaseResponseBody> joinApplyRejectOwner(@PathVariable Long userid
			, @RequestBody JoinApplyReq joinApplyReq) {
		
		// 해당 study_apply 정보 가져오기(어차피 하나밖에 없다)
		StudyApply studyApply = joinService.getStudyApplyReceiver(userid, joinApplyReq);

		if(studyApply == null)
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
		
		joinService.modifyJoinApply(studyApply);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "가입 거절 완료"));
	}
	
	@PostMapping("/{userId}")
	@ApiOperation(value="가입 하기", notes="사용자의 가입 하기")
	public ResponseEntity<? extends BaseResponseBody> joinStudy(@PathVariable Long userId
			, Long studyId) {


		String res = joinService.studyApply(studyId, userId);
		if("population".equals(res)) { // 인원초과일 경우
			return ResponseEntity.status(201).body(BaseResponseBody.of(201, "인원 초과입니다."));
		} else if("ok".equals(res)) { // 가입 승인 완료
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "스터디 가입 요청을 보냈습니다."));
		} else if("deadline".equals(res)) { // 모집 날짜 초과
			return ResponseEntity.status(201).body(BaseResponseBody.of(201, "마감한 스터디입니다."));
		} else if("already".equals(res)) { // 이미 가입 신청함
			return ResponseEntity.status(201).body(BaseResponseBody.of(201, "이미 신청한 스터디 입니다."));
		} else if("invitation".equals(res)) { // 이미 가입 신청함
			return ResponseEntity.status(201).body(BaseResponseBody.of(201, "초대 현황을 확인해보세요."));
		} else { // 그 외
			return ResponseEntity.status(400).body(BaseResponseBody.of(400, "다시 시도해 주세요."));
		}
	}
}
