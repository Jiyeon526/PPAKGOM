package com.ppakgom.api.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendGetRes {
	
	List<AttendRes> attendList; //유저별 출석 현황
	String user_name;
	Long user_id;
	int all; //전체 스터디
	int attended; //출석일.
	
	public AttendGetRes() {this.setAttendList(new ArrayList<>());}
}
