package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("User Register Post Request Model")
public class UserRegisterPostReq {
	
	@ApiModelProperty(name = "userId", example = "SSAFY")
	String userId;
	
	@ApiModelProperty(name = "password", example = "SSAFY")
	String password;
	
	@ApiModelProperty(name = "email", example = "SSAFY@naver.com")
	String email;
	
	@ApiModelProperty(name = "name", example = "SSAFY")
	String name;
	
	@ApiModelProperty(name = "interest", example = "")
	String[] interest;
	
}
