package com.ppakgom.api.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
/**
 * 방 생성 API ([POST] /api/v1/conferences/conferences) 
 * 요청에 필요한 리퀘스트 바디 정의.
 */

@Getter
@Setter
@ApiModel("ConferenceRegisterPostReq")
public class ConferenceRegisterPostReq {
	@ApiModelProperty(name = "conference_category", example = "1")
	Long conference_category;
	
	@ApiModelProperty(name = "title", example = "방 제목")
	String title;
	
	@ApiModelProperty(name = "description", example = "방 설명")
	String description;
	
	
	@Override
	public String toString() {
		return "ConferenceRegisterPostReq [conference_category=" + conference_category + ", title=" + title
				+ ", description=" + description + "]";
	}
}
