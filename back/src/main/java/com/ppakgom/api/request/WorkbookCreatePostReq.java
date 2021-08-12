package com.ppakgom.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 문제집 생성 몸체
 */
@Getter
@Setter
@ApiModel("Workbook Post(create) Request")

public class WorkbookCreatePostReq {

	Test test;
	String[] answer;
	
}
