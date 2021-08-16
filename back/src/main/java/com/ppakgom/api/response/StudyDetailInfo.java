package com.ppakgom.api.response;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyDetailInfo {
	
	@ApiModelProperty(name="study_plan_id", example="1")
	Long study_plan_id;
	
	@ApiModelProperty(name="title", example="ssafy")
	String title;
	
	@ApiModelProperty(name="date", example="2010-01-01")
	Date date;
	
	@ApiModelProperty(name="attend", example="")
	List<MemberAttend> studyAttend;
	
}	
