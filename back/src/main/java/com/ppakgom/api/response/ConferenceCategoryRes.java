package com.ppakgom.api.response;

import com.ppakgom.db.entity.ConferenceCategory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리 목록 조회 ([GET] /api/v1/conferences/conference-category) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("Conference Category Response")
public class ConferenceCategoryRes {
	@ApiModelProperty(name="Category ID", example="1")
	Long id;
	@ApiModelProperty(name="Category Name", example="업무")
	String name;
	
	public ConferenceCategoryRes of(ConferenceCategory category) {
		ConferenceCategoryRes res = new ConferenceCategoryRes();
		res.setId(category.getId());
		res.setName(category.getName());
		return res;
	}
	
}



