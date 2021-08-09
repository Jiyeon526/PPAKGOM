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
@ApiModel("Cancel Invitation")
public class CancelInviteReq {

	@ApiModelProperty(name="studyId", example="50")
	Long studyId;
	
	@ApiModelProperty(name="receiverId", example="3")
	Long receiverId;

}
