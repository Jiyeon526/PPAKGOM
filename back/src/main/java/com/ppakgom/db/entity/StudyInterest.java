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
	
	public StudyInterest(Interest i, Study s) {
		this.interest = i ;
		this.study = s;
	}
}
