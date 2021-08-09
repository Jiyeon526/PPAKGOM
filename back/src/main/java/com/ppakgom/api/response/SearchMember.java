package com.ppakgom.api.response;

import java.util.ArrayList;
import java.util.List;

import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserStudy;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 관심사로 회원 검색 시 
 * 응답 값
 * 
 */

@Getter
@Setter
@ApiModel("회원 목록")
public class SearchMember {

//	user_id, 닉네임, 썸네일, 열정도, 가입한 스터디 목록
//	가입한 스터디 목록
	User user;
	List<Study> study;

	public SearchMember() {}
	
	public SearchMember (User user, List<UserStudy> studyList) {
		this.user = user;
		study = new ArrayList<>();
		for(UserStudy us : studyList) {
			study.add(us.getStudy());
		}
	}

}
