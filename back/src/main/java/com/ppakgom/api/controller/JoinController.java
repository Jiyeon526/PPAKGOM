package com.ppakgom.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ppakgom.api.response.JoinApplyListRes;
import com.ppakgom.api.service.JoinService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
}
