package com.ppakgom.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomData {
	
	@ApiModelProperty(name="title", example="일정")
	String title;
	
	@ApiModelProperty(name="color", example="red")
	String color;

	public CustomData(String title, String color) {
		super();
		this.title = title;
		this.color = color;
	}

	public CustomData() {
		super();
		// TODO Auto-generated constructor stub
	}

}
