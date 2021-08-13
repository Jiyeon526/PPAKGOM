package com.ppakgom.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginReq {
	
	@ApiModelProperty(name="user_id", example="ssafy")
	String user_id;
	
	@ApiModelProperty(name="email", example="ssafy@naver.com")
	String email;
	
	@ApiModelProperty(name="name", example="ssafy")
	String name;
}
