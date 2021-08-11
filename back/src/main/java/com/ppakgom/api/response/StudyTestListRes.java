package com.ppakgom.api.response;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("스터디 문제집 리스트 결과")
public class StudyTestListRes {

	@ApiModelProperty(name="id", example="3")
	Long id;
	
	@ApiModelProperty(name="name", example="ssafy")
	String name;
	
	@ApiModelProperty(name="title", example="문제집")
	String title;

	public StudyTestListRes(Long id, String name, String title) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
	}

	public StudyTestListRes() {
		super();
	}

}
