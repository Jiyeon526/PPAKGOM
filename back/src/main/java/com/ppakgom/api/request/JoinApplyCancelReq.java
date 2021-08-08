package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Join Apply Cancel Request")
public class JoinApplyCancelReq {
	
	@ApiModelProperty(name="study_id", example="3")
	Long study_id;
	
	@ApiModelProperty(name="receiver_id", example="40")
	Long receiver_id;
}
