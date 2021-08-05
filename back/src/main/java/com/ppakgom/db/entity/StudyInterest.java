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
public class StudyInterest extends BaseEntity{
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id")
	Interest interest;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	Study study;
	
	public StudyInterest(Long s, Long i) {
		this.interest = new Interest();
		this.getInterest().setId(i);
		this.study = new Study();
		this.study.setId(s);
	}
}
