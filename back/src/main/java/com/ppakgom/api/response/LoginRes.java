package com.ppakgom.api.response;

import com.ppakgom.common.model.response.BaseResponseBody;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 시도시 응답값
 * 상태코드, 메시지, 로그인성공시 엑세스 토큰도 함께 리턴
 */

@Getter
@Setter
@ApiModel("로그인 응답값")
public class LoginRes extends BaseResponseBody{
	
	@ApiModelProperty(name="JWT 인증 토큰", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN...")
	String accessToken;
	
	@ApiModelProperty(name="사용자 ID(PRIMARY KEY)", example="3")
	Long id;
	
	public LoginRes(Integer statusCode, String message, String accessToken, Long id) {
		this.setStatusCode(statusCode);
		this.setMessage(message);
		this.setAccessToken(accessToken);
		this.setId(id);
	}
	
	public LoginRes() {}

	

}
