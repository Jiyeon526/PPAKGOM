package com.ppakgom.api.request;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	@ApiModelProperty(name="name", example="빡곰 스터디")
	String name;

	@ApiModelProperty(name="content", example="빡곰 스터디는 원활한 스터디를 지원합니다.")
	String content;
	
	@ApiModelProperty(name="interest", example="[프로그래밍, 개발자]")
	String[] interest;
	
	@ApiModelProperty(name="temperature", example="38.5")
	float temperature;

	@ApiModelProperty(name="deadline", example="2021-09-23")
	String deadline;
	
	@ApiModelProperty(name = "population",example = "5")
	int population;

	@Override
	public String toString() {
		return "StudyCreatePostReq [name=" + name + ", content=" + content + ", interest=" + Arrays.toString(interest)
				+ ", temperature=" + temperature + ", deadline=" + deadline + ", population=" + population + "]";
	}
	
	
	
}
