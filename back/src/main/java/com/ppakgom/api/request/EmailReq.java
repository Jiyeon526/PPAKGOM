package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Login Request")
public class EmailReq {
	@ApiModelProperty(name="email", example="ssafy@naver.com")
	String email;
	@ApiModelProperty(name="code", example="111111")
	String code;
}
