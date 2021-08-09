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
@ApiModel("Reject Invitation")
public class InviteReq_sender {

	@ApiModelProperty(name="studyId", example="50")
	Long studyId;
	
	@ApiModelProperty(name="senderId", example="3")
	Long senderId;

}
