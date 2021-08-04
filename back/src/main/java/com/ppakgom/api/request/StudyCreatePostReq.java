package com.ppakgom.api.request;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 몸체
 */
@Getter
@Setter
@ApiModel("Study Post(create) Request")
public class StudyCreatePostReq {

	@ApiModelProperty(name="스터디 이름", example="빡곰 스터디")
	String name;

	@ApiModelProperty(name="스터디 설명", example="빡곰 스터디는 원활한 스터디를 지원합니다.")
	String content;
	
	@ApiModelProperty(name="스터디 관심사", example="[프로그래밍, 개발자]")
	String[] interest;
	
	@ApiModelProperty(name="열정도", example="38.5")
	float temperature;
	
	@ApiModelProperty(name="마감날짜", example="2021-09-23")
	Date deadline;
	
	@ApiModelProperty(name = "모집인원",example = "5")
	int population;
	
}
