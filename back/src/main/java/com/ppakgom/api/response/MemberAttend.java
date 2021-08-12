package com.ppakgom.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberAttend {
	
	@ApiModelProperty(name="user_id", example="1")
	Long user_id;
	
	@ApiModelProperty(name="attend", example="T")
	boolean attend;

}
