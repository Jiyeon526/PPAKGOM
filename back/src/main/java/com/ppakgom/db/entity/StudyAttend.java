package com.ppakgom.db.entity;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudyAttend extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	Study study;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_plan_id")
	StudyPlan studyPlan;
	
	boolean isAttend;

	public StudyAttend(Study study, User user, StudyPlan studyPlan, boolean isAttend) {
		super();
		this.study = study;
		this.user = user;
		this.studyPlan = studyPlan;
		this.isAttend = isAttend;
	}

	public StudyAttend() {
		super();
		// TODO Auto-generated constructor stub
	}

}
