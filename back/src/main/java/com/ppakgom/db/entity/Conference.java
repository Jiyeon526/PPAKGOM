package com.ppakgom.db.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Conference extends BaseEntity{
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conferece_category")
	ConferenceCategory conferenceCategory;
	
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "owner_id")
	 User user;

	boolean isActive;
	
	String thumbnailUrl;
	String title;
	String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	Date callStartTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date callEndTime;
	
	@OneToMany(mappedBy = "conference")
	List<UserConference> list = new ArrayList<>();

	@Override
	public String toString() {
		return "Conference [conferenceCategory=" + conferenceCategory + ", user=" + user + ", isActive=" + isActive
				+ ", thumbnailUrl=" + thumbnailUrl + ", title=" + title + ", description=" + description
				+ ", callStartTime=" + callStartTime + ", callEndTime=" + callEndTime + ", list=" + list + "]";
	}
	
	
	
}
