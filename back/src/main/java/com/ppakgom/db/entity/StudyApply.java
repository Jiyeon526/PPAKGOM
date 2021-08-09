package com.ppakgom.db.entity;

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

	boolean isJoin;
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

	public StudyApply() {
	}

	public StudyApply(User sender, Study study, User receiver, boolean is_join, Short state) {
		this.sender = sender;
		this.state = state;
		this.study = study;
		this.receiver = receiver;
		this.isJoin = is_join;
	}

	@Override
	public String toString() {
		return "StudyApply [is_join=" + isJoin + ", state=" + state + ", receiver=" + receiver + ", sender=" + sender
				+ ", study=" + study + "]";
	}

}