package com.ppakgom.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("가입 신청 현황 응답값")
public class JoinApplyListRes {
	
	@ApiModelProperty(name="study_id", example="32")
	Long study_id;
	
	@ApiModelProperty(name="study_name", example="마이크 들리시조")
	String study_name;
	
	@ApiModelProperty(name="owner_user_name", example="마이크")
	String owner_user_name;
	
	@ApiModelProperty(name="state", example="2")
	Short state;
}
