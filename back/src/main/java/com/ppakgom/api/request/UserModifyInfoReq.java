package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("User Modify Request Model")
public class UserModifyInfoReq {
	
	@ApiModelProperty(name = "name", example = "SSAFY")
	String name;
	
	@ApiModelProperty(name = "interest", example = "SSAFY")
	String[] interest;
}
