package com.ppakgom.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyTestInfoRes {
	
	@ApiModelProperty(name="answer", example="3,4,5,6")
	String[] answer;
	
	@ApiModelProperty(name="test", example="")
	StudyTests test;
}
