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
public class StudyScore extends BaseEntity {
	
	short score;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	Study study;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	StudyTest studyTest;
}
