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
	
	@ApiModelProperty(name="customData", example="")
	CustomData customData;

	@ApiModelProperty(name="date", example="2021-09-23 00:00:00.000000")
	Date dates;

	public StudyScheduleMonthRes(Long id, CustomData customData, Date date) {
		super();
		this.id = id;
		this.customData = customData;
		this.dates = date;
	}

	public StudyScheduleMonthRes() {
		super();
		// TODO Auto-generated constructor stub
	}

}
