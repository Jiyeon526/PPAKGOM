package com.ppakgom.api.response;

import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyRate;
import com.ppakgom.db.entity.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RateRes {
	
	Long study_id;
	String name;
	String study_member_name;
	boolean checked;
	Long studyMemberId; //명세서에 카멜케이스라 적혀
	
	public RateRes(StudyRate rate) {
		Study study = rate.getStudy();
		User user = rate.getStudyMember();
		
		this.setStudy_id(study.getId());
		this.setName(study.getName());
		this.setStudy_member_name(user.getName());
		this.setChecked(rate.isChecked());
		this.setStudyMemberId(user.getId());
	}
}
