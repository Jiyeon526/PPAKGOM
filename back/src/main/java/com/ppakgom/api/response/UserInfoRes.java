package com.ppakgom.api.response;

import java.util.List;

import com.ppakgom.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("자신의 프로필 확인")
public class UserInfoRes {
	
	@ApiModelProperty(name="user_id", example="ssafy")
	String user_id;
	
	@ApiModelProperty(name="email", example="ssafy@naver.com")
	String email;
	
	@ApiModelProperty(name="name", example="ssafy")
	String name;
	
	@ApiModelProperty(name="interest", example="[취직, 취업]")
	List<String> interest;
	
	@ApiModelProperty(name="profile_thumbnail")
	String profile_thumbnail;

	public static UserInfoRes of(User user, List<String> interest) {
		UserInfoRes res = new UserInfoRes();
		
		res.setUser_id(user.getUserId());
		res.setEmail(user.getEmail());
		res.setName(user.getName());
		res.setProfile_thumbnail(user.getProfile_thumbnail());
		res.setInterest(interest);
	
		return res;
	}
}
