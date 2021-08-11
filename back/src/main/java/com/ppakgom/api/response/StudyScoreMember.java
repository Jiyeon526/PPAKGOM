package com.ppakgom.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("스터디 멤버당 문제 푼 결과들")
public class StudyScoreMember {
	
	@ApiModelProperty(name="test_title", example="카카오 하반기 코딩 테스트 예상 문제집")
	String test_title;
	
	@ApiModelProperty(name="score", example="10")
	Short score;
	
}
