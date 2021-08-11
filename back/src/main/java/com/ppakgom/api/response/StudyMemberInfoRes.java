package com.ppakgom.api.response;

import java.util.ArrayList;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudyMemberInfoRes {
	
	@ApiModelProperty(name="name", example="닉네임")
	String name;
	
	@ApiModelProperty(name="user_id", example="아이디")
	String user_id;
	
	@ApiModelProperty(name="id", example="3")
	Long id;
	
	@ApiModelProperty(name="thumbnail", example="사진 주소")
	String thumbnail;

	public StudyMemberInfoRes(String name, String user_id, Long id, String thumbnail) {
		super();
		this.name = name;
		this.user_id = user_id;
		this.id = id;
		this.thumbnail = thumbnail;
	}

	public StudyMemberInfoRes() {
		super();
		// TODO Auto-generated constructor stub
	}

}
