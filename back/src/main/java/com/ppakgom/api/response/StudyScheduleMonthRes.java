package com.ppakgom.api.response;

import java.util.Date;
import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("스터디 방 스케쥴")
public class StudyScheduleMonthRes {

	@ApiModelProperty(name="id", example="36")
	Long id;
	
	@ApiModelProperty(name="title", example="일정")
	String title;
	
	@ApiModelProperty(name="detail", example="일정 설명")
	String detail;
	
	@ApiModelProperty(name="date", example="2021-09-23 00:00:00.000000")
	Date date;

	public StudyScheduleMonthRes(Long id, String title, String detail, Date date) {
		super();
		this.id = id;
		this.title = title;
		this.detail = detail;
		this.date = date;
	}

	public StudyScheduleMonthRes() {
		super();
		// TODO Auto-generated constructor stub
	}

}
