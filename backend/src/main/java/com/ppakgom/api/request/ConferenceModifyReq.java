package com.ppakgom.api.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ConferenceModifyRequest")
public class ConferenceModifyReq {
	@ApiModelProperty(name = "conference_category", example = "1")
	Long conference_category;
	@ApiModelProperty(name = "title", example = "방 제목")
	String title;
	@ApiModelProperty(name = "description", example = "방 설명")
	String description;
	@Override
	public String toString() {
		return "ConferenceModifyReq [conference_category=" + conference_category + ", title=" + title + ", description="
				+ description + "]";
	}
	
//	@ApiModelProperty(name = "Thumbnail Img", example = "방 썸네일")
//	MultipartFile thumbnailImg;
}
