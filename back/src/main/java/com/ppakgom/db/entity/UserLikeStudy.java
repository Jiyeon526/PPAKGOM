package com.ppakgom.db.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;


/**
 * 사용자가 찜한 스터디.
 */

@Entity
@Getter
@Setter
public class UserLikeStudy extends BaseEntity{
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	Study study;
	
	public UserLikeStudy(User u, Study s) {
		this.user = u;
		this.study = s;
	}
	
	public UserLikeStudy() {}
}
