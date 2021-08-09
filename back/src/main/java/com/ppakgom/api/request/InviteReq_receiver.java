package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 초대 취소 몸체
 */
@Getter
@Setter
@ApiModel("Cancel Invitation")
public class InviteReq_receiver {

	@ApiModelProperty(name="studyId", example="50")
	Long studyId;
	
	@ApiModelProperty(name="receiverId", example="3")
	Long receiverId;

}
