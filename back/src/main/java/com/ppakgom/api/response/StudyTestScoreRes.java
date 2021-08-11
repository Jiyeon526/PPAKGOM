package com.ppakgom.api.response;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("문제 푼 결과")
public class StudyTestScoreRes {
	
	@ApiModelProperty(name="number", example="10")
	Short number;
	
	@ApiModelProperty(name="correct", example="7")
	Short correct;
}
