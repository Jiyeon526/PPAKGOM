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
public class StudyInvitePostReq {
	
	@ApiModelProperty(name="receiver_id", example="3")
	Long receiver_id;

	@ApiModelProperty(name="is_join", example="true")
	boolean is_join;

}
