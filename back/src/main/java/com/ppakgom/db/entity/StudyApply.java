package com.ppakgom.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudyApply extends BaseEntity {

	boolean is_join;
	Short state;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	User receiver;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	User sender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	Study study;

	public StudyApply(boolean is_join, Short state, User receiver, User sender, Study study) {
		super();
		this.is_join = is_join;
		this.state = state;
		this.receiver = receiver;
		this.sender = sender;
		this.study = study;
	}

	public StudyApply() {
		super();
	}

}
