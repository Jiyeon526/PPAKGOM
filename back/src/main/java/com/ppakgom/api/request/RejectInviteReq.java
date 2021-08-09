package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 초대 거절 몸체
 */
@Getter
@Setter
@ApiModel("Cancel Invitation")
public class RejectInviteReq {

	@ApiModelProperty(name="studyId", example="50")
	Long studyId;
	
	@ApiModelProperty(name="receiverId", example="3")
	Long receiverId;

}
