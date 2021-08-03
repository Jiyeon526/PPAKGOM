package com.ppakgom.api.response;

import java.util.Date;

import com.ppakgom.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Conference Users")
public class ConferenceUsers {
	@ApiModelProperty(name="User ID", example="test-1")
	String user_id;
	@ApiModelProperty(name="User Name", example="참여자 1")
	String name;
	
	public static ConferenceUsers of(User user) {
		ConferenceUsers res = new ConferenceUsers();
		res.setUser_id(user.getUserId());
		res.setName(user.getName());
		return res;
	}
}
