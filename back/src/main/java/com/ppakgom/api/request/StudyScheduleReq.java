package com.ppakgom.api.request;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Study Schedule Request")
public class StudyScheduleReq {
	
	@ApiModelProperty(name="title", example="ssafy 일정")
	String title;
	
	@ApiModelProperty(name="date", example="2021-09-23")
	String date;
	
	@ApiModelProperty(name="color", example="red")
	String color;
}
