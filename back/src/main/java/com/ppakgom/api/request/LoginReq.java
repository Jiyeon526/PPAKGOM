package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 몸체
 */
@Getter
@Setter
@ApiModel("Login Request")
public class LoginReq {

	@ApiModelProperty(name="id", example="ssafy_web")
	String id;
	@ApiModelProperty(name="password", example="your_password")
	String password;

}
