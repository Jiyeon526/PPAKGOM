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
	
	@ApiModelProperty(name="detail", example="ssafy 일정 - 다음주 수요일, 32쪽까지")
	String detail;
	
	@ApiModelProperty(name="date", example="2021-09-23")
	String date;
}
