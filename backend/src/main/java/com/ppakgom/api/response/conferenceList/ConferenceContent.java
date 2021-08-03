package com.ppakgom.api.response.conferenceList;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.ppakgom.db.entity.Conference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConferenceContent {
	Long id;
	String conference_category;
	int joinUsersNum;
	String owner;
	@Temporal(TemporalType.TIMESTAMP)
	Date callStartTime;
	String thumbnail_url;
	String title;
	String description;
	
//	Conference를 받으면 ConferenceContent로 변환 후 반환
	public static ConferenceContent of(Conference c) {
		ConferenceContent res = new ConferenceContent();
		res.setId(c.getId());
		res.setConference_category(c.getConferenceCategory().getName());
		res.setJoinUsersNum(c.getList().size());
		res.setOwner(c.getUser().getName());
		res.setCallStartTime(c.getCallStartTime());
		res.setTitle(c.getTitle());
		res.setDescription(c.getDescription());
		System.out.println("res "+res);
		return res;
	}

	@Override
	public String toString() {
		return "ConferenceContent [id=" + id + ", conference_category=" + conference_category + ", joinUsersNum="
				+ joinUsersNum + ", owner=" + owner + ", callStartTime=" + callStartTime + ", thumbnail_url="
				+ thumbnail_url + ", title=" + title + ", description=" + description + "]";
	}
	
	
}
