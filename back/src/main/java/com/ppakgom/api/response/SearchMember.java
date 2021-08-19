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

//	 id, usrid, 프로필, 온도, 닉네임 만 추출..
//	User user;
	Long id;
	String user_id;
	String thumbnail;
	float temperature;
	String user_name;
//	가입한 스터디 목록
//	List<Study> study;

	public SearchMember() {}
	
	public SearchMember (User user, List<UserStudy> studyList) {
		this.setId(user.getId());
		this.setUser_id(user.getUserId());
		this.setThumbnail(user.getProfile_thumbnail());
		this.setTemperature(user.getTemperature());
		this.setUser_name(user.getName());
//		study = new ArrayList<>();
//		for(UserStudy us : studyList) {
//			study.add(us.getStudy());
//		}
	}

}
