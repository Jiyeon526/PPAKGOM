package com.ppakgom.api.response;

import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.StudyApply;

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
@ApiModel("초대받은 현황")
public class InvitedRes {
	
	@ApiModelProperty(name="study_id", example="2")
	Long studyId;
	
	@ApiModelProperty(name="study_name", example="빡곰 스터디")
	String studyName;

	@ApiModelProperty(name="user_name", example="김철수")
	String userName;
	
	@ApiModelProperty(name="state", example="2")
	Short state;
	
	public InvitedRes of(StudyApply sa) {
		this.setStudyId(sa.getStudy().getId());
		this.setStudyName(sa.getStudy().getName());
		this.setUserName(sa.getSender().getName());
		this.setState(sa.getState());
		return this;
	}
	
	public InvitedRes() {}

	

}
