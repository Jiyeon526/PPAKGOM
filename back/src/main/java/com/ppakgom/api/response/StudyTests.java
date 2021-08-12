package com.ppakgom.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyTests {

	@ApiModelProperty(name="id", example="")
	Long id;
	
	@ApiModelProperty(name="user_id", example="")
	String user_id;
	
	@ApiModelProperty(name="title", example="")
	String title;
	
	@ApiModelProperty(name="test_url", example="")
	String test_url;

	public StudyTests(Long id, String user_id, String title, String test_url) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.title = title;
		this.test_url = test_url;
	}

	public StudyTests() {
		super();
		// TODO Auto-generated constructor stub
	}

}
