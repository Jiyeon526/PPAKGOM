package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("User Register Post Request Model")
public class UserRegisterPostReq {
	
	@ApiModelProperty(name = "유저 ID", example = "SSAFY")
	String userId;
	
	@ApiModelProperty(name = "유저 Password", example = "SSAFY")
	String password;
	
	@ApiModelProperty(name = "유저 Email", example = "SSAFY@naver.com")
	String email;
	
	@ApiModelProperty(name = "유저 Name", example = "SSAFY")
	String name;
	
	@ApiModelProperty(name = "유저 Interest", example = "")
	String[] interest;
}
