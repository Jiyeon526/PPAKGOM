package com.ppakgom.api.response;

import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 초대 현황 요청시 응답값 
 * 스터디 ID, 스터디 이름, 유저 ID, 상태
 * 
 */

@Getter
@Setter
@ApiModel("초대한 현황")
public class InviteResByStudy {

	Long userId;
	String name;
	String thumbnail;
	float temperature;
	
	public InviteResByStudy of(StudyApply sa) {
		User user = sa.getReceiver();
		this.setUserId(user.getId());
		this.setName(user.getName());
		this.setThumbnail(user.getProfile_thumbnail());
		this.setTemperature(user.getTemperature());
		return this;
	}
	
	public InviteResByStudy() {}

	

}
