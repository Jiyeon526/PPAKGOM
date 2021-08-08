package com.ppakgom.api.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ppakgom.api.request.JoinApplyReq;
import com.ppakgom.api.response.JoinApplyListRes;
import com.ppakgom.api.service.JoinService;
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.StudyApply;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Api(value="가입 API", tags = {"Join"})
@RestController
@RequestMapping("api/v1/join")
public class JoinController {
	
	@Autowired
	JoinService joinService;
	
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
}
