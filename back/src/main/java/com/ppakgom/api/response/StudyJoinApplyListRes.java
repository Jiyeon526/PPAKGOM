package com.ppakgom.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("스터디 내에서 가입 요청 현황 응답값")
public class StudyJoinApplyListRes {
	
	@ApiModelProperty(name="user_id", example="ssafy")
	String user_id;
	
	@ApiModelProperty(name="name", example="ssafy")
	String name;
	
	@ApiModelProperty(name="thumbnail", example="C:주소")
	String thumbnail;
	
	@ApiModelProperty(name="temperature", example="36.5")
	float temperature;
	
	@ApiModelProperty(name="state", example="2")
	Short state;
}
