package com.ppakgom.api.response;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("스터디 문제 푼 결과들")
public class StudyTestScoreTotalRes {
	
	@ApiModelProperty(name="name", example="닉네임")
	String name;
	
	@ApiModelProperty(name="data", example="")
	List<StudyScoreMember> data;
}
