package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 스터디 평가 기록시 요청객체 (POST)
 *
 */
@Getter
@Setter
@ApiModel("Study rate(POST) Request")
public class StudyRatePostReq {
	
	@ApiModelProperty(name="studyId", example="3")
	Long studyId;
	
	@ApiModelProperty(name="studyMemberId", example="1")
	Long studyMemberId;
	
	@ApiModelProperty(name="rating", example="4")
	int rating;
}
