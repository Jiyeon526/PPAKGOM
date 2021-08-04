package com.ppakgom.api.response;

import com.ppakgom.db.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("스터디 생성 값")
public class StudyCreatePostRes extends BaseEntity{
	
	Long id;
	
	public StudyCreatePostRes(Long s) {
		this.id = s;
	}
}
