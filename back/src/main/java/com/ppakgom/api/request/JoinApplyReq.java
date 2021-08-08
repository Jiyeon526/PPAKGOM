package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Join Apply Cancel Request")
public class JoinApplyReq {
	
	@ApiModelProperty(name="study_id", example="3")
	Long study_id;
	
	@ApiModelProperty(name="user_id", example="40")
	Long user_id;
}
