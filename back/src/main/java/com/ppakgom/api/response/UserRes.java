package com.ppakgom.api.response;

import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 본인 정보 조회 API ([GET] /api/v1/users/me) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("UserResponse")
public class UserRes{
	@ApiModelProperty(name="User Department", example="SSAFY")
	String department;
	@ApiModelProperty(name="User Position", example="교육생")
	String position;
	@ApiModelProperty(name="User Name", example="홍길동")
	String name;
	@ApiModelProperty(name="User ID", example="test-1")
	String userId;
	
	public static UserRes of(User user) {
		UserRes res = new UserRes();
		res.setDepartment(user.getDepartment());
		res.setPosition(user.getPosition());
		res.setName(user.getName());
		res.setUserId(user.getUserId());
		return res;
	}
}

